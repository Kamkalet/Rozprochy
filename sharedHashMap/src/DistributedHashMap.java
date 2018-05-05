import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.protocols.*;
import org.jgroups.protocols.pbcast.*;
import org.jgroups.stack.ProtocolStack;
import proto.DistributedMapOperation;
import proto.Operation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DistributedHashMap implements SimpleStringMap {

    private JChannel channel;
    private final Map<String, String> sharedMap;
    MapReceiver receiver;
    String clusterName ;
    InetAddress address;

    DistributedHashMap(String clusterName, String address) {
        this.sharedMap = new ConcurrentHashMap<>();
        this.clusterName = clusterName;
        try {
            this.address = InetAddress.getByName(address);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    DistributedHashMap(String clusterName) {
        this.sharedMap = new ConcurrentHashMap<>();
        this.clusterName = clusterName;
    }

    public void init() throws Exception {
        this.channel = new JChannel();
        ProtocolStack stack = new ProtocolStack();
        channel.setProtocolStack(stack);
        UDP udp = new UDP();
        if (address != null) {
            udp.setValue("mcast_group_addr", address);
        }
        stack.addProtocol(udp)
                .addProtocol(new PING())
                .addProtocol(new MERGE3())
                .addProtocol(new FD_SOCK())
                .addProtocol(new FD_ALL()
                        .setValue("timeout", 12000)
                        .setValue("interval", 3000))
                .addProtocol(new VERIFY_SUSPECT())
                .addProtocol(new BARRIER())
                .addProtocol(new NAKACK2())
                .addProtocol(new UNICAST3())
                .addProtocol(new STABLE())
                .addProtocol(new GMS())
                .addProtocol(new UFC())
                .addProtocol(new MFC())
                .addProtocol(new FRAG2())
                .addProtocol(new SEQUENCER())
                .addProtocol(new STATE_TRANSFER())
                .addProtocol(new FLUSH())
                ;

        stack.init();
        receiver = new MapReceiver(this, sharedMap, channel);
        channel.setReceiver(receiver);
        channel.connect(clusterName);
        channel.getState(null, 10000);
    }

    public void closeChannel() {
        this.channel.close();
    }


    @Override
    public boolean containsKey(String key) {
        return sharedMap.containsKey(key);
    }

    @Override
    public String get(String key) {
        return this.sharedMap.get(key);
    }

    @Override
    public String put(String key, String value) {
        String prevValue = sharedMap.get(key);
        DistributedMapOperation op = new DistributedMapOperation(Operation.PUT, key, value);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        byte[] bytes = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(op);
            out.flush();
            bytes = bos.toByteArray();
        } catch (IOException ex) {
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Message message = new Message(null, null, bytes);
        try {
            channel.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prevValue;
    }

    @Override
    public String remove(String key) {
        String value = sharedMap.get(key);
        DistributedMapOperation op = new DistributedMapOperation(Operation.REMOVE, key, null);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        byte[] bytes = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(op);
            out.flush();
            bytes = bos.toByteArray();
        } catch (IOException ex) {
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Message message = new Message(null, null, bytes);
        try {
            channel.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public JChannel getChannel() {
        return channel;
    }

    public Map<String, String> getSharedMap() {
        return sharedMap;
    }

    public MapReceiver getReceiver() {
        return receiver;
    }
}

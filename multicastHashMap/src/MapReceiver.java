import org.jgroups.*;
import org.jgroups.util.Util;
import proto.DistributedMapOperation;

import java.io.*;
import java.util.Map;

public class MapReceiver extends ReceiverAdapter {

    private final SimpleStringMap state;
    private final Map<String, String> sharedMap;
    JChannel channel;

    public MapReceiver(SimpleStringMap distributedHashMap,  Map<String, String> sharedMap, JChannel channel) {
        this.state = distributedHashMap;
        this.sharedMap = sharedMap;
        this.channel = channel;
    }

    @Override
    public void receive(Message msg) {

        byte[] bytes = msg.getBuffer();
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInput in = null;
        Object o = null;
        try {
            in = new ObjectInputStream(bis);
            o = in.readObject();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
        }
        DistributedMapOperation dMO = (DistributedMapOperation) o;
        System.out.println(dMO);
        doOperation(dMO);

    }

    private void doOperation(DistributedMapOperation dMO) {

        switch (dMO.getOperation()){

            case PUT:
                sharedMap.put(dMO.getKey(), dMO.getValue());
                break;
            case REMOVE:
                sharedMap.remove(dMO.getKey());
                break;
        }

    }

    @Override
    public void getState(OutputStream output) throws Exception {
        synchronized (sharedMap) {
            Util.objectToStream(sharedMap, new DataOutputStream(output));
        }
    }

    @Override
    public void setState(InputStream input) throws Exception {
        final Map<String, String> mapFromState
                = (Map<String, String>) Util.objectFromStream(new DataInputStream(input));
        synchronized (sharedMap) {
            sharedMap.clear();
            sharedMap.putAll(mapFromState);
        }
    }

    @Override
    public void viewAccepted(View newView) {
        handleView(channel, newView);
    }

    private void handleView(JChannel channel, View newView) {
        if(newView instanceof MergeView) {
            ViewHandler handler = new ViewHandler(channel, (MergeView) newView);
            handler.start();
        }
    }


}

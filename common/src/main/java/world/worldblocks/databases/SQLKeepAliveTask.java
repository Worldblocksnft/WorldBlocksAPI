package world.worldblocks.databases;

public class SQLKeepAliveTask implements Runnable {

    private SQLClient sqlClient;
    public SQLKeepAliveTask(SQLClient sqlClient) {
        this.sqlClient = sqlClient;
    }

    @Override
    public void run() {
        sqlClient.keepAlive();
    }

}

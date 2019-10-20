package com.forkexec.pts.ws.cli;

import com.forkexec.pts.ws.*;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINamingException;

import javax.xml.ws.Response;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class PointsFrontEnd {

    /** Collection of PointsClients registered in UDDI */
    private List<PointsClient> pointsClients = new ArrayList<>();
    /** Constant number of servers registered in UDDI */
    private final int nServers;

    /** Cache for maintaing information about recent users */
    private PointsCache cache = new PointsCache();

    /** constructor with provided active points clients' urls */
    public PointsFrontEnd(UDDINaming uddiNaming) throws PointsFrontEndException {
        Collection<String> pointsServers;
        try {
            pointsServers = uddiNaming.list("A61_Points%");
        } catch (UDDINamingException e) {
            System.err.println("Error connecting to UDDI" + e);
            throw new RuntimeException(e);
        }
        nServers = pointsServers.size();
        try {
            for (String url : pointsServers)
                pointsClients.add(new PointsClient(url));
        }
        catch(PointsClientException e) {
            System.err.println("Error creating Points Client" + e);
            throw new PointsFrontEndException(e);
        }

    }

    public void activateUser(String userEmail) throws EmailAlreadyExistsFault_Exception, InvalidEmailFault_Exception, InterruptedException {
        System.out.println("--- USER " + userEmail + " ACTIVATE ACCOUNT ---");
        this.read(userEmail);
        System.out.println();

    }

    public int accountBalance (String userEmail) throws InvalidEmailFault_Exception, InterruptedException {
        System.out.println("--- USER " + userEmail + " ACCOUNT BALANCE ---");
        int res = read(userEmail).getBalance();
        System.out.println();
        return res;

    }

    public BalanceInfo read(String userEmail) throws InvalidEmailFault_Exception, InterruptedException {
        System.out.println("Started read operation");
        /* check if user info is in cache, if it is does not need to apply quorum consensus */
        BalanceInfo cachedInfo = this.cache.find(userEmail);
        if (cachedInfo != null) {
            System.out.println("Used value saved in cache");
            return cachedInfo;
        }

        Map<Response<PointsBalanceResponse>, Boolean> responseList = new HashMap<>();
        for(PointsClient client : pointsClients){
            Response<PointsBalanceResponse> response = client.pointsBalanceAsync(userEmail);
            responseList.put(response, false);
        }

        int balance = -1;
        int tag = -1;
        int counter = 0;

        polling:
        while (counter < (nServers / 2 + 1)) {
            for (Map.Entry<Response<PointsBalanceResponse>, Boolean> response : responseList.entrySet()) {

                if (response.getKey().isDone() && !response.getValue()) {
                    counter++;
                    responseList.put(response.getKey(), true);
                    try {
                        System.out.println("Receive response number " + counter);
                        System.out.println("Balance: " + response.getKey().get().getReturn().getBalance());

                        int responseTag = response.getKey().get().getReturn().getTag();
                        System.out.println("Tag: " + responseTag);

                        if (tag < responseTag) {
                            tag = responseTag;
                            balance = response.getKey().get().getReturn().getBalance();
                        }
                        System.out.println("Higher tag updated: " + tag);
                    } catch (ExecutionException e) {
                        System.out.println("Caught execution exception.");
                        System.out.print("Cause: ");
                        System.out.println(e.getCause());
                    }
                }
                // Quorum Consensus majority of servers achieved
                if (counter >= (nServers / 2 + 1)) break polling;
            }
            Thread.sleep(10);
            System.out.print(".");
            System.out.flush();
        }
        System.out.println("Received " + counter + " server responses");
        BalanceInfo balanceInfo = new BalanceInfo();
        balanceInfo.setTag(tag);
        balanceInfo.setBalance(balance);

        /* updates cache with most recent info after read */
        this.cache.put(userEmail, balanceInfo);

        return balanceInfo;
	}

    public void addPoints (String userEmail, int pointsToAdd) throws InvalidEmailFault_Exception, InvalidPointsFault_Exception, InterruptedException {
        System.out.println("--- ADDING " + pointsToAdd + " POINTS TO ACCOUNT: " + userEmail + " ---");
        write(userEmail, pointsToAdd);
        System.out.println();
    }

    public void spendPoints (String userEmail, int pointsToSpend) throws InvalidEmailFault_Exception, InvalidPointsFault_Exception, InterruptedException {
        System.out.println("--- SPENDING " + pointsToSpend + " POINTS OF ACCOUNT: " + userEmail + " ---");
        write(userEmail, -pointsToSpend);
        System.out.println();

    }

    public void write(String userEmail, int points) throws InvalidEmailFault_Exception, InvalidPointsFault_Exception, InterruptedException {
        BalanceInfo balanceInfo = this.read(userEmail);
        System.out.println();
        System.out.println("Started write operation");

        /* update balance info */
        balanceInfo.setBalance(balanceInfo.getBalance() + points);
        balanceInfo.setTag(balanceInfo.getTag() + 1);

        Map<Response<SetBalanceResponse>, Boolean> responseList = new HashMap<>();
        for(PointsClient client : pointsClients){
            Response<SetBalanceResponse> response = client.setBalanceAsync(userEmail,
                    balanceInfo.getBalance(), balanceInfo.getTag());
            responseList.put(response, false);
        }

        int counter = 0;

        polling:
        while (counter < (nServers / 2 + 1)) {
            for (Map.Entry<Response<SetBalanceResponse>, Boolean> response : responseList.entrySet()) {

                if (response.getKey().isDone() && !response.getValue()) {
                    counter++;
                    System.out.println("Receive response number " + counter);
                    responseList.put(response.getKey(), true);
                }
                // Quorum Consensus majority of servers achieved
                if (counter >= (nServers / 2 + 1)) break polling;
            }
            Thread.sleep(10);
            System.out.print(".");
            System.out.flush();
        }
        System.out.println("Received " + counter + " server responses");

        /* updates cache with most recent info after write */
        this.cache.put(userEmail, balanceInfo);
    }

    public String ctrlPing(String inputMessage) throws ExecutionException, InterruptedException {
        Response<CtrlPingResponse> r = pointsClients.get(0).ctrlPingAsync(inputMessage);
                Thread.sleep(30000);
                return r.get().getReturn();

    }
}

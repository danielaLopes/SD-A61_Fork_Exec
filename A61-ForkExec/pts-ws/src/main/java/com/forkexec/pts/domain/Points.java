package com.forkexec.pts.domain;

import com.forkexec.pts.domain.Exceptions.*;
import com.forkexec.pts.ws.BalanceInfo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Points
 * <p>
 * A points server.
 */
public class Points {

    /**
     * Constant representing the default initial balance for every new client
     */
    private final int DEFAULT_INITIAL_BALANCE = 100;
    private Map<String, BalanceInfoPoints> userInfo = new ConcurrentHashMap<>();
    /**
     * Global with the current value for the initial balance of every new client
     */
    private final AtomicInteger initialBalance = new AtomicInteger(DEFAULT_INITIAL_BALANCE);

    // Singleton -------------------------------------------------------------

    /**
     * Private constructor prevents instantiation from other classes.
     */
    private Points() { }

    /**
     * SingletonHolder is loaded on the first execution of Singleton.getInstance()
     * or the first access to SingletonHolder.INSTANCE, not before.
     */
    private static class SingletonHolder {
        private static final Points INSTANCE = new Points();
    }

    public static synchronized Points getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public synchronized void containsEmail(String userEmail) throws EmailAlreadyExistsException {
        if (userInfo.containsKey(userEmail)){
            throw new EmailAlreadyExistsException("Email already exists");
        }
    }

    public synchronized void validatesEmail(String userEmail) throws InvalidEmailException {
        if (!userEmail.matches("^(([A-Za-z0-9]+\\.)*[A-Za-z0-9]+)@(([A-Za-z0-9]+\\.)*[A-Za-z0-9]+)$")){
            throw new InvalidEmailException("Invalid Email : not a valid email");
        }
    }

    public synchronized void DoesNotContainEmail(String userEmail) throws InvalidEmailException{
        if (!userInfo.containsKey(userEmail)){
            throw new InvalidEmailException("Invalid email: user doesn't exist");
        }
    }

    public synchronized void activateUser(final String userEmail) throws EmailAlreadyExistsException , InvalidEmailException {
        validatesEmail(userEmail);
        containsEmail(userEmail);
        //AtomicInteger i = new AtomicInteger(initialBalance.get());
        BalanceInfoPoints balanceInfoPoints = new BalanceInfoPoints(initialBalance.get(), 0);
        userInfo.put(userEmail, balanceInfoPoints);

    }

    public synchronized BalanceInfoPoints pointsBalance(final String userEmail) throws InvalidEmailException {
        validatesEmail(userEmail);
        //DoesNotContainEmail(userEmail);
        BalanceInfoPoints info = userInfo.get(userEmail);
        if (info == null){
            BalanceInfoPoints balanceInfoPoints = new BalanceInfoPoints(initialBalance.get(), 0);
            userInfo.put(userEmail, balanceInfoPoints);
            return balanceInfoPoints;
        }
        return info;
    }

    public synchronized int addPoints(final String userEmail, final int pointsToAdd)
            throws InvalidEmailException, InvalidPointsException {

        /*int totalPoints;
        validatesEmail(userEmail);

        DoesNotContainEmail(userEmail);

        if (pointsToAdd <= 0){
            throw new InvalidPointsException("Invalid Points");
        }

        totalPoints = userInfo.get(userEmail).addAndGet(pointsToAdd);*/

        return -1;
    }

    public synchronized int spendPoints(final String userEmail, final int pointsToSpend)
            throws InvalidEmailException, InvalidPointsException, NotEnoughBalanceException {

        /*int totalPoints;
        validatesEmail(userEmail);

        DoesNotContainEmail(userEmail);

        if (pointsToSpend <= 0){
          throw new InvalidPointsException("Invalid Points");
        }

        totalPoints = userInfo.get(userEmail).get() - pointsToSpend;

        if (totalPoints < 0){
          throw new NotEnoughBalanceException("Not Enough Balance");
        }
        else{
            userInfo.get(userEmail).addAndGet(-pointsToSpend);
        }*/

        return -1;
    }

    public int setBalance(final String userEmail, final int balance, final int tag)
            throws InvalidEmailException, InvalidPointsException {
        if (balance <= 0){
            throw new InvalidPointsException("Invalid Points");
        }
        validatesEmail(userEmail);
        BalanceInfoPoints balanceInfoPoints = new BalanceInfoPoints(balance, tag);
        userInfo.put(userEmail, balanceInfoPoints);
        return -1;
    }

    public void ctrlInit(final int startPoints) throws BadInitException {
        if(startPoints <= 0){
            throw new BadInitException("Invalid initial balance");
        }
        initialBalance.set(startPoints);
    }

    public void ctrlClear() {
        userInfo.clear();
        initialBalance.set(DEFAULT_INITIAL_BALANCE);
    }
}

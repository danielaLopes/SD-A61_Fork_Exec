package com.forkexec.pts.ws;

import com.forkexec.pts.domain.Exceptions.*;
import com.forkexec.pts.domain.Points;
import com.forkexec.pts.domain.BalanceInfoPoints;
import javax.jws.WebService;

/**
 * This class implements the Web Service port type (interface). The annotations
 * below "map" the Java class to the WSDL definitions.
 */
@WebService(endpointInterface = "com.forkexec.pts.ws.PointsPortType", wsdlLocation = "PointsService.wsdl", name = "PointsWebService", portName = "PointsPort", targetNamespace = "http://ws.pts.forkexec.com/", serviceName = "PointsService")
public class PointsPortImpl implements PointsPortType {

    /**
     * The Endpoint manager controls the Web Service instance during its whole
     * lifecycle.
     */
    private final PointsEndpointManager endpointManager;

    /** Constructor receives a reference to the endpoint manager. */
    public PointsPortImpl(final PointsEndpointManager endpointManager) {
	this.endpointManager = endpointManager;
    }

    // Main operations -------------------------------------------------------

    @Override
	public void activateUser(final String userEmail) throws EmailAlreadyExistsFault_Exception, InvalidEmailFault_Exception {
        try {
            Points.getInstance().activateUser(userEmail);
        }
        catch (EmailAlreadyExistsException e){
            throwEmailAlreadyExists(e.getMessage());
        }
        catch (InvalidEmailException e){
            throwInvalidEmail(e.getMessage());
        }
    }

    @Override
    public BalanceInfo pointsBalance(final String userEmail) throws InvalidEmailFault_Exception {
        BalanceInfo info = new BalanceInfo();
        try {
            info = balanceInfoPointsToBalanceInfo(Points.getInstance().pointsBalance(userEmail));
        }
        catch (InvalidEmailException e){
            throwInvalidEmail(e.getMessage());
        }
        return info;
    }

    @Override
    public int addPoints(final String userEmail, final int pointsToAdd)
	    throws InvalidEmailFault_Exception, InvalidPointsFault_Exception {
        int add = -1;
        try {
            add = Points.getInstance().addPoints(userEmail, pointsToAdd);
        }
        catch (InvalidEmailException e){
            throwInvalidEmail(e.getMessage());
        }
        catch (InvalidPointsException e){
            throwInvalidPoints(e.getMessage());
        }
        return add;
    }

    @Override
    public int spendPoints(final String userEmail, final int pointsToSpend)
	    throws InvalidEmailFault_Exception, InvalidPointsFault_Exception, NotEnoughBalanceFault_Exception {
        int spend = -1;
        try {
            spend =  Points.getInstance().spendPoints(userEmail, pointsToSpend);
        }
        catch (InvalidEmailException e){
            throwInvalidEmail(e.getMessage());
        }
        catch (InvalidPointsException e){
            throwInvalidPoints(e.getMessage());
        }
        catch (NotEnoughBalanceException e){
            throwNotEnoughBalance(e.getMessage());
        }
        return spend;
    }

    @Override
    public int setBalance(final String userEmail, final int balance, final int tag)
            throws InvalidEmailFault_Exception, InvalidPointsFault_Exception {
        int spend = -1;
        try {
            spend =  Points.getInstance().setBalance(userEmail, balance, tag);
        }
        catch (InvalidEmailException e){
            throwInvalidEmail(e.getMessage());
        }
        catch (InvalidPointsException e){
            throwInvalidPoints(e.getMessage());
        }
        return spend;
    }

    // Control operations ----------------------------------------------------
    /** Diagnostic operation to check if service is running. */
    @Override
    public String ctrlPing(String inputMessage) {
        if (inputMessage.equals("slow")) {
            /*try {
                System.out.println("Server thread name: " + Thread.currentThread().getId());
                //Thread.sleep(30000); // server sleeps for half a minute
            } catch (InterruptedException e) {
                System.err.println("Error in making server sleep " + e);
                throw new RuntimeException(e);
            }*/
        }

        if (inputMessage.equals("die")) {
            try {
                this.endpointManager.stop();
            } catch (Exception e) {
                System.err.println("Error in stopping server " + e);
            }
        }

        if (inputMessage.equals("exception")) {
            try {
                throw new Exception();
            } catch (Exception e) {
                System.err.println("Server is dying with exceptions! " + e);
            }
        }

        // If no input is received, return a default name.
        if (inputMessage == null || inputMessage.trim().length() == 0)
            inputMessage = "friend";

        // If the park does not have a name, return a default.
        String wsName = endpointManager.getWsName();
        if (wsName == null || wsName.trim().length() == 0)
            wsName = "Park";

        // Build a string with a message to return.
        final StringBuilder builder = new StringBuilder();
        builder.append("Hello ").append(inputMessage);
        builder.append(" from ").append(wsName);
        return builder.toString();
    }

    /** Return all variables to default values. */
    @Override
    public void ctrlClear() {
        Points.getInstance().ctrlClear();
    }

    /** Set variables with specific values. */
    @Override
    public void ctrlInit(final int startPoints) throws BadInitFault_Exception {
        try {
            Points.getInstance().ctrlInit(startPoints);
        }
        catch (BadInitException e){
            throwBadInit(e.getMessage());
        }
    }

    public BalanceInfo balanceInfoPointsToBalanceInfo(BalanceInfoPoints infoPoints){
        BalanceInfo info = new BalanceInfo();
        info.setBalance(infoPoints.getBalance());
        info.setTag(infoPoints.getTag());   
        return info;
    }


    // Exception helpers -----------------------------------------------------

    private void throwInvalidEmail(final String message) throws InvalidEmailFault_Exception {
        final InvalidEmailFault faultInfo = new InvalidEmailFault();
        faultInfo.message = message;
        throw new InvalidEmailFault_Exception(message, faultInfo);
    }

    private void throwEmailAlreadyExists(final String message) throws EmailAlreadyExistsFault_Exception {
        final EmailAlreadyExistsFault faultInfo = new EmailAlreadyExistsFault();
        faultInfo.message = message;
        throw new EmailAlreadyExistsFault_Exception(message, faultInfo);
    }

    private void throwInvalidPoints(final String message) throws InvalidPointsFault_Exception {
        final InvalidPointsFault faultInfo = new InvalidPointsFault();
        faultInfo.message = message;
        throw new InvalidPointsFault_Exception(message, faultInfo);
    }

    private void throwNotEnoughBalance(final String message) throws NotEnoughBalanceFault_Exception {
        final NotEnoughBalanceFault faultInfo = new NotEnoughBalanceFault();
        faultInfo.message = message;
        throw new NotEnoughBalanceFault_Exception(message, faultInfo);
    }

    /** Helper to throw a new BadInit exception. */
    private void throwBadInit(final String message) throws BadInitFault_Exception {
        final BadInitFault faultInfo = new BadInitFault();
        faultInfo.message = message;
        throw new BadInitFault_Exception(message, faultInfo);
    }
}

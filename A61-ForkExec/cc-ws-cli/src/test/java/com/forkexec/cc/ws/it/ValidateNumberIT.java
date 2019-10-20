package com.forkexec.cc.ws.it;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class ValidateNumberIT extends BaseIT{

    @Test
    public void validNumber() {

        assertTrue(client.validateNumber("4024007102923926"));
    }

    @Test
    public void invalidNumber() {

        assertFalse(client.validateNumber("100"));
    }
}

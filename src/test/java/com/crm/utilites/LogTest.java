package com.crm.utilites;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogTest {

    private static final Logger logger = LoggerFactory.getLogger(LogTest.class);

    public static void main(String[] args) {

        logger.info("Starting API test");

        // Example log for an API response
        logger.debug("Received response: {status: 200, body: 'OK'}");

        logger.error("Test failed due to unexpected status code");

    }

}

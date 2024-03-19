package com.csye6225.assignment.webapp.service.impl;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
public class SentiveInformationFilter extends Filter<ILoggingEvent> {



        @Override
        public FilterReply decide(ILoggingEvent event) {
            // Check if the log message contains sensitive information
            String message = event.getMessage();
                if (message.contains("password")) {
                // Do not log messages containing passwords
                return FilterReply.DENY;
            }
            // Log all other messages
            return FilterReply.NEUTRAL;
        }
    }


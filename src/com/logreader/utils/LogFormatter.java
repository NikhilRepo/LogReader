package com.logreader.utils;

import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class LogFormatter extends Formatter{
	@Override
    public String format(LogRecord logRecord) {
        return logRecord.getSourceClassName()+"::"
                +logRecord.getSourceMethodName()+"::"
                +new Date(logRecord.getMillis())+"::"
                +logRecord.getMessage()+"\n";
    }
}

package com.nutrisystem.orange.java.scheduled;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.nutrisystem.orange.java.constant.AppConstant;
import com.nutrisystem.orange.utility.aws.S3Util;
import com.nutrisystem.orange.utility.date.DateUtil;

@Component
public class ScheduledLogBackupTasks {
    @Value("${folder}")
    private String folder;

    @Scheduled(cron = "0 0 * * * *")
    @Async
    public void transferLogs() {
	moveLogs(AppConstant.SERVERLOG_RELATIVE_PATH, AppConstant.SERVERLOG_PREFIX, "server.log");
	moveLogs(AppConstant.ACCESSLOG_RELATIVE_PATH, AppConstant.ACCESSLOG_PREFIX, null);
	moveLogs(AppConstant.ADMINACCESSLOG_RELATIVE_PATH, AppConstant.ADMINACCESSLOG_PREFIX, null);
	moveLogs(AppConstant.DATALOG_RELATIVE_PATH, AppConstant.DATALOG_PREFIX, "data.log");
    }

    private void moveLogs(String relativePathToLogs, String logFilePrefix, String currentLogFile) {
	Path currPath = Paths.get("");
	Path pathToLogs = Paths.get(currPath.toAbsolutePath().toString() + relativePathToLogs);
	File[] files = pathToLogs.toFile().listFiles();

	System.out.println(DateUtil.getCurrentTimeStamp() + pathToLogs.toString() + "/" + logFilePrefix
		+ " logs transfer starts.");

	// process server logs.
	List<File> logFileList = new ArrayList<File>();
	for (File file : files) {
	    if (file.getName().equals(currentLogFile) || file.getName().startsWith(logFilePrefix))
		logFileList.add(file);
	}

	S3Util.putAppLogFiles(logFileList, folder + "/" + AppConstant.getNodeName());

	List<File> logFileToRemoveList = new ArrayList<File>();
	for (File file : logFileList) {
	    if (!file.getName().equals(currentLogFile) && !file.getName().contains(DateUtil.getDateString(new Date()))
		    && !file.getName().contains(DateUtil.getDateString(DateUtil.addDays(new Date(), -1))))
		logFileToRemoveList.add(file);
	}

	for (File file : logFileToRemoveList)
	    file.delete();

	System.out.println(DateUtil.getCurrentTimeStamp() + pathToLogs.toString() + "/" + logFilePrefix
		+ "logs transfer is done.");
    }
}

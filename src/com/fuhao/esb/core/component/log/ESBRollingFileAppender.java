package com.fuhao.esb.core.component.log;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.Writer;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.helpers.CountingQuietWriter;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.helpers.OptionConverter;
import org.apache.log4j.spi.LoggingEvent;

/**
 * package name is  com.fuhao.esb.core.component.log
 * Created by fuhao on 13-12-10.
 * Project Name esb-java
 */
public class ESBRollingFileAppender extends FileAppender  {


    protected long maxFileSize = 10485760L;
    protected int maxBackupIndex = 0;

    private boolean calcIndex = false;
    private volatile int currentBackupIndex = 0;
    private volatile int minBackupIndex = Integer.MAX_VALUE;
    private long nextRollover = 0L;

    public ESBRollingFileAppender() {
    }

    public ESBRollingFileAppender(Layout layout, String filename, boolean append) throws IOException {
        super(layout, filename, append);
        calcMaxAndMinBackupIndex(filename);
    }

    public ESBRollingFileAppender(Layout layout, String filename) throws IOException {
        super(layout, filename);
        calcMaxAndMinBackupIndex(filename);
    }

    private final void calcMaxAndMinBackupIndex(final String filename) {
        if (this.calcIndex) {
            return;
        }

        final File target = new File(fileName);
        final File path = target.getParentFile();

        for (String name : path.list(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.startsWith(target.getName() + ".");
            }
        })) {
            int currentIndex = Integer.valueOf(name.substring(name.lastIndexOf(".") + 1));
            if (currentIndex > this.currentBackupIndex) {
                this.currentBackupIndex = currentIndex;
            }
            if (currentIndex < this.minBackupIndex) {
                this.minBackupIndex = currentIndex;
            }
        }

        nextBackupIndex();

        this.calcIndex = true;
    }

    // ----------------------------------------------------------------------------------------------------------

    protected void subAppend(LoggingEvent event) {
        super.subAppend(event);
        if ((this.fileName != null) && (this.qw != null)) {
            long size = ((CountingQuietWriter) this.qw).getCount();
            if ((size >= this.maxFileSize) && (size >= this.nextRollover))
                rollOver();
        }
    }

    public void rollOver() {
        calcMaxAndMinBackupIndex(this.fileName);

        if (this.qw != null) {
            long size = ((CountingQuietWriter) this.qw).getCount();
            LogLog.debug("rolling over count=" + size);

            this.nextRollover = (size + this.maxFileSize);
        }
        LogLog.debug("maxBackupIndex=" + this.currentBackupIndex);

        File file = null;
        boolean renameSucceeded = true;

        if (this.currentBackupIndex - this.minBackupIndex >= this.maxBackupIndex) {
            file = new File(this.fileName + '.' + this.minBackupIndex);
            if (file.exists()) {
                renameSucceeded = file.delete();
                this.minBackupIndex++;
            }
        }

        if (renameSucceeded) {
            File target = new File(getNextBackupFileName());

            closeFile();

            file = new File(this.fileName);
            LogLog.debug("Renaming file " + file + " to " + target);
            renameSucceeded = file.renameTo(target);

            if (!renameSucceeded) {
                try {
                    setFile(this.fileName, true, this.bufferedIO, this.bufferSize);
                } catch (IOException e) {
                    LogLog.error("setFile(" + this.fileName + ", true) call failed.", e);
                }
            } else {
                nextBackupIndex();
            }
        }

        if (renameSucceeded) {
            try {
                setFile(this.fileName, false, this.bufferedIO, this.bufferSize);
                this.nextRollover = 0L;
            } catch (IOException e) {
                LogLog.error("setFile(" + this.fileName + ", false) call failed.", e);
            }
        }
    }

    public final String getNextBackupFileName() {
        return this.fileName + "." + this.currentBackupIndex;
    }

    public synchronized void setFile(String fileName, boolean append, boolean bufferedIO, int bufferSize) throws IOException {
        super.setFile(fileName, append, this.bufferedIO, this.bufferSize);
        if (append) {
            File f = new File(fileName);
            ((CountingQuietWriter) this.qw).setCount(f.length());
        }
    }

    // ----------------------------------------------------------------------------------------------------------

    private int nextBackupIndex() {
        return ++this.currentBackupIndex;
    }

    public int getMaxBackupIndex() {
        return this.maxBackupIndex;
    }

    public void setMaxBackupIndex(int maxBackups) {
        this.maxBackupIndex = maxBackups;
    }

    public long getMaximumFileSize() {
        return this.maxFileSize;
    }

    public void setMaximumFileSize(long maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    public void setMaxFileSize(String value) {
        this.maxFileSize = OptionConverter.toFileSize(value, this.maxFileSize + 1L);
    }

    protected void setQWForFiles(Writer writer) {
        this.qw = new CountingQuietWriter(writer, this.errorHandler);
    }
}

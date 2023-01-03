package org.dsc.utilties;

import java.io.*;


public class metaDataWriter {
	private String outFolder;
	private String outFile;

	public void setOutFolder(String outFolder) {
		this.outFolder = outFolder;
	}

	public void setOutFile(String outFile) {
		this.outFile = outFile;
	}

	public void writeMetaDataFile(InputStream metaData) {
		try (OutputStream output = new FileOutputStream(outFolder+"/"+outFile, false)) {
            metaData.transferTo(output);
        } catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

}

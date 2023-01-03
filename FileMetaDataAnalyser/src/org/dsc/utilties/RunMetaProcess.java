package org.dsc.utilties;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Set;

public class RunMetaProcess {

	private String inFolder;
	private String outFolder;

	public void setInFolder(String inFolder) throws IOException {
		if (Files.exists(Paths.get(inFolder))) {
			this.inFolder = inFolder;
		} else {
			throw new IOException("Input Folder does not exist");
		}
	}

	public void setOutFolder(String outFolder) throws IOException {
		if (Files.exists(Paths.get(outFolder))) {
			this.outFolder = outFolder;
		} else {
			throw new IOException("Output Folder does not exist");
		}

	}


	public void run(postGres pg) throws IOException, InterruptedException, SQLException, ClassNotFoundException {
	
		jsonReader jsonRead = new jsonReader();
		jsonRead.setPg(pg);
		folderProcessor fp = new folderProcessor();
		Set<File> fileList = null;
		fileList = fp.listFilesUsingFilesList(inFolder);
		tikaRequest tr = new tikaRequest();
		tr.setServiceUrl("http://localhost:9998/meta");
		for (File fl : fileList) {
			tr.setUploadFile(fl.getAbsolutePath());
			InputStream resp = null;
			resp = tr.tikaRequest();
			metaDataWriter mdr = new metaDataWriter();
			mdr.setOutFile(fl.getName() + ".meta");
			mdr.setOutFolder(outFolder);
			mdr.writeMetaDataFile(resp);
			File metaFile = new File (outFolder+"/"+fl.getName()+".meta");
			jsonRead.processMetaFile(metaFile);
		}
		jsonRead.closeConnection();
	}
}

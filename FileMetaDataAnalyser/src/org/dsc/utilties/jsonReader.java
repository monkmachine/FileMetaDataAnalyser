package org.dsc.utilties;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.json.JsonReadFeature;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;

public class jsonReader {

    JsonFactory jfactory = JsonFactory.builder()
            .enable(JsonReadFeature.ALLOW_MISSING_VALUES,
                    JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS,
                    JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER,
                    JsonReadFeature.ALLOW_UNQUOTED_FIELD_NAMES,
                    JsonReadFeature.ALLOW_TRAILING_COMMA,
                    JsonReadFeature.ALLOW_NON_NUMERIC_NUMBERS,
                    JsonReadFeature.ALLOW_LEADING_ZEROS_FOR_NUMBERS)
            .build();
    JsonParser jParser;
    postGres pg;

    public void setPg(postGres pg) {
		this.pg = pg;
	}

	public jsonReader() throws SQLException {
    }

    public void processMetaFile(File metaFile) throws IOException, SQLException, ClassNotFoundException {
        long bytes = Files.size(metaFile.toPath());
        if (bytes > 0) {
            jParser = jfactory.createParser(metaFile);

            JsonToken nextToken;
            jParser.nextToken();
            while ((nextToken = jParser.nextToken()) != JsonToken.END_OBJECT && jParser.hasCurrentToken()) {
                if (nextToken == JsonToken.FIELD_NAME) {
                    String nextFieldName = jParser.currentName();
                    nextToken = jParser.nextToken();
                    if (nextToken.isStructStart()) {
                        while (jParser.nextToken() != JsonToken.END_ARRAY) {
                            String val = jParser.getValueAsString();
                            pg.runStatement(metaFile.getName(), nextFieldName, val);

                        }
                    }
                    if (jParser.getValueAsString() != null) {
                        String val = jParser.getValueAsString();
                        pg.runStatement(metaFile.getName(), nextFieldName, val);
                    }
                }

            }
            pg.commit();
        }
    }

    public void closeConnection() throws SQLException {
        pg.pgCloseConection();

    }

}

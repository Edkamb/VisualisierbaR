package com.github.bachelorpraktikum.dbvisualization.datasource;

import com.github.bachelorpraktikum.dbvisualization.config.ConfigKey;
import com.github.bachelorpraktikum.dbvisualization.database.ABSExporter;
import com.github.bachelorpraktikum.dbvisualization.database.Database;
import com.github.bachelorpraktikum.dbvisualization.model.Context;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;
import javax.annotation.Nonnull;

public class DatabaseSource implements DataSource {

    private static final Logger log = Logger.getLogger(DatabaseSource.class.getName());

    private final InputParserSource inputParserSource;
    private final InputStream inputStream;
    private final String ABS_COMPILER_BINARY_NAME = "absc";

    /**
     * Creates a {@link Database}-Source that reads info from the database, compiles the ABS file
     * and reads the output.
     *
     * @param absFilePath the path to the *.abs file which has to be compiled
     * @throws IOException if an error during the abs export occured
     * @throws IOException if the subprocess can't be started
     */
    public DatabaseSource(@Nonnull Database database, String absFilePath)
        throws IOException {
        ABSExporter exporter = database.getExporter();
        absFilePath = getAbsFilePath(absFilePath, exporter);
        exportFromDatabase(exporter, absFilePath);

        SubprocessSource inputSource = new SubprocessSource(getAbsExecutable(), absFilePath);
        this.inputParserSource = inputSource;
        this.inputStream = inputSource.getInputStream();
    }

    /**
     * Starts the {@link ABSExporter}
     * Uses the <code>absFilePath</code> as export/read location.
     *
     * @param exporter Database to read from
     * @param absFilePath File to write ABS export to and compile for later reading
     */
    private void exportFromDatabase(ABSExporter exporter, String absFilePath) throws IOException {
        if (!exporter.export(absFilePath)) {
            log.severe("Export was unsuccessful. Not continuing with exporter source.");
            throw new IOException("Exporting the .abs file failed.");
        }
    }

    /**
     * If <code>absFilePath</code> is null, the path is retrieved from {@link
     * ABSExporter#getExportPath()}.
     *
     * @param absFilePath Possible path for the ABS file
     * @param exporter Exporter to read <code>absFilePath</code> from if not defined already
     * @return path for the abs file
     */
    private String getAbsFilePath(String absFilePath, @Nonnull ABSExporter exporter) {
        if (absFilePath == null) {
            absFilePath = exporter.getExportPath();
        }

        return absFilePath;
    }

    /**
     * Tries to read the abs location from the {@link com.github.bachelorpraktikum.dbvisualization.config.ConfigFile
     * config file}, otherwise assumes that the ABS-Compiler <tt>absc</tt> is in the <tt>PATH</tt>
     */
    private String getAbsExecutable() {
        String absToolchain = "absc";
        String absToolchainConfig = ConfigKey.absToolchain.get();
        if (absToolchainConfig != null) {
            if (absToolchain.endsWith("absc")) {
                absToolchain = absToolchainConfig;
            } else {
                absToolchain = String.join(File.pathSeparator, absToolchain,
                    ABS_COMPILER_BINARY_NAME);
            }
        } else {
            absToolchain = ABS_COMPILER_BINARY_NAME;
        }

        return absToolchain;
    }

    @Nonnull
    @Override
    public Context getContext() {
        return inputParserSource.getContext();
    }

    @Override
    public void close() throws IOException {
        inputParserSource.close();
        log.info("Successfully closed Database-Source");
    }
}

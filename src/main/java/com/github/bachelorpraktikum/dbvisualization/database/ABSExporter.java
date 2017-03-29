package com.github.bachelorpraktikum.dbvisualization.database;

import com.github.bachelorpraktikum.dbvisualization.config.ConfigKey;
import com.github.bachelorpraktikum.dbvisualization.database.model.ABSExportable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.StringJoiner;
import java.util.logging.Logger;

public class ABSExporter {

    private final List<ABSExportable> elements;
    private boolean exportString;
    private final String DEFAULT_FILENAME = "Run.abs";

    ABSExporter() {
        this(new LinkedList<>());
    }

    ABSExporter(List<ABSExportable> elements) {
        this.elements = elements;
    }

    private String getEnclosingFunctionFormattable() {
        String lineSep = System.lineSeparator();
        return String.format("Unit run() {%s%s%s%s}", lineSep, "%s", "%s", lineSep);
    }

    private String getLineSeperatedElements() {
        return getLineSeperatedElements(4);
    }

    private String getLineSeperatedElements(int spacesBeforeLine) {
        StringJoiner lineSeperatorJoiner = new StringJoiner(System.lineSeparator());
        elements.forEach(absExportable -> lineSeperatorJoiner
            .add(leftPad(absExportable.export(), spacesBeforeLine)));
        elements.forEach(absExportable -> {
            for (String childrenExport : absExportable.exportChildren()) {
                if (!childrenExport.isEmpty()) {
                    for (String sub : childrenExport.split(System.lineSeparator())) {
                        lineSeperatorJoiner.add(leftPad(sub, spacesBeforeLine));
                    }
                }
            }
        });

        return lineSeperatorJoiner.toString();
    }

    private String leftPad(String string, int number) {
        if (number <= 0) {
            return string;
        }

        StringBuilder sb = new StringBuilder(string);
        int charsToGo = 4;
        while (charsToGo > 0) {
            sb.insert(0, ' ');
            charsToGo--;
        }

        return sb.toString();
    }

    private String getExtra() {
        return "";
    }

    /**
     * <p>If a path is defined in the {@link com.github.bachelorpraktikum.dbvisualization.config.ConfigFile}
     * via {@link com.github.bachelorpraktikum.dbvisualization.config.ConfigKey#absExportPath}, the
     * ABS export will be written to that file.</p>
     *
     * <p>If no path is defined, <code>DEFAULT_FILENAME</code> will be used as the export
     * location.</p>
     *
     * @return Whether the writting of the file was successfull
     */
    public boolean export() {
        String filename = ConfigKey.absExportPath.get();
        if (filename == null) {
            filename = DEFAULT_FILENAME;
        }

        return export(filename);
    }

    /**
     * Writes the exported elements into <code>filename</code>
     *
     * @return Whether the writting of the file was successfull
     */
    public boolean export(String filename) {
        File file = new File(filename);
        if (file.exists()) {
            if (!deleteFile(file)) {
                Logger.getLogger(getClass().getName())
                    .info(String.format("Couldn't delete file (%s), abort writing.",
                        file.getAbsolutePath()));
            }
        }
        try (OutputStream outputStream = new FileOutputStream(filename)) {
            file.createNewFile();
            export(outputStream);
        } catch (IOException e) {
            String message = String.format("Couldn't export to %s: \n%s", filename, e);
            Logger.getLogger(getClass().getName()).severe(message);
            return false;
        }

        return true;
    }

    private boolean deleteFile(File file) {
        Logger.getLogger(getClass().getName())
            .info(String.format("Deleting %s", file.getAbsolutePath()));
        return file.delete();
    }

    /**
     * Writes the abs content (enclosed in a `Run` function) to the given stream.
     *
     * @param outputStream Stream to write abs export to
     * @throws IOException Thrown if writting to the <code>outputStream</code> was unsuccessful
     */
    public void export(OutputStream outputStream) throws IOException {
        outputStream.write(getExportString().getBytes());
    }

    public List<ABSExportable> getElements() {
        return elements;
    }

    private String getExportString() {
        return String
            .format(getEnclosingFunctionFormattable(), getLineSeperatedElements(), getExtra());
    }
}

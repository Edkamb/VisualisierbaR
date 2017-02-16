package com.github.bachelorpraktikum.dbvisualization.view;

import com.github.bachelorpraktikum.dbvisualization.DataSource;
import com.github.bachelorpraktikum.dbvisualization.config.ConfigFile;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DatabaseChooserController implements SourceChooser {

    @FXML
    private BorderPane rootPaneDatabase;
    @FXML
    private TextField ipField;
    @FXML
    public Label uriError;
    @FXML
    private TextField databaseNameField;
    @FXML
    private TextField portField;

    private ReadOnlyObjectWrapper<URI> databaseURIProperty;
    private ReadOnlyObjectWrapper<String> databaseNameProperty;
    private ReadOnlyObjectWrapper<Integer> portProperty;

    @FXML
    public void initialize() {
        databaseURIProperty = new ReadOnlyObjectWrapper<>();
        databaseNameProperty = new ReadOnlyObjectWrapper<>();
        portProperty = new ReadOnlyObjectWrapper<>();
        loadInitialValues();

        ipField.textProperty().addListener((o, oldValue, newValue) -> {
            if (newValue == null || newValue.trim().isEmpty()) {
                databaseURIProperty.set(null);
                return;
            }
            URI uri = null;
            try {
                uri = new URI(newValue);
                databaseURIProperty.set(uri);
                check();
            } catch (URISyntaxException ignored) {
                String message = String.format("%s is not a valid URI.", newValue);
                Logger.getLogger(getClass().getName()).info(message);
            } finally {
                // Display the error message if the URI hasn't been set
                uriError.setVisible(uri == null);
            }
        });

        databaseNameProperty.bindBidirectional(databaseNameField.textProperty());
        databaseNameProperty.addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                databaseNameProperty.set(newValue);
                check();
            } else {
                databaseNameProperty.set(null);
            }
        });

        portField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                int port = Integer.valueOf(newValue);
                portProperty.set(port);
                check();
            } catch (NumberFormatException ignored) {
                portProperty.set(null);
            }
        });
    }

    private void loadInitialValues() {
        String configKey = getInitialDirKey();
        String uriString = (String) ConfigFile.getInstance().getOrDefault(configKey, "");
        if (!uriString.isEmpty()) {
            try {
                URI uri = URI.create(uriString);
                ipField.setText(String.format("%s://%s", uri.getScheme(), uri.getHost()));
                portField.setText(String.valueOf(uri.getPort()));
                databaseNameField.setText(uri.getPath().substring(1));
            } catch (IllegalArgumentException e) {
                String message = String.format("URI from config isn't valid:\n%s", e);
                Logger.getLogger(getClass().getName()).info(message);
            }
        }
    }

    private String getInitialDirKey() {
        String logFileKey = ResourceBundle.getBundle("config_keys")
            .getString("initialDirectoryKey");
        return String.format(logFileKey, getResourceType().toString());
    }

    private void check() {
        if (databaseURIProperty.get() != null
            && databaseNameProperty.get() != null
            && portProperty.get() != null) {
            enableOpen();
        }
    }

    private void enableOpen() {
        rootPaneDatabase.getScene().lookup("#openSource").setDisable(false);
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override

    public URI getResourceURI() {
        return databaseURIProperty.getValue();
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public ReadOnlyObjectProperty<URI> resourceURIProperty() {
        return databaseURIProperty.getReadOnlyProperty();
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public String getRootPaneId() {
        return rootPaneDatabase.getId();
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public DataSource.Type getResourceType() {
        return DataSource.Type.DATABASE;
    }

    @Override
    public void setInitialURI(URI initialURI) {
        ipField.setText(initialURI.getPath());
    }
}

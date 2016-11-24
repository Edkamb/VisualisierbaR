package com.github.bachelorpraktikum.dbvisualization.view;

import com.github.bachelorpraktikum.dbvisualization.DataSource;

import java.net.URL;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import javafx.beans.property.ReadOnlyProperty;

/**
 * Represents a chooser for a data source.<br>The user can input a URL by which the data source can
 * be accessed.<br>The chooser does not check whether that is the case.<br>The data which can be
 * retrieved from the source has to be compatible with the described {@link
 * com.github.bachelorpraktikum.dbvisualization.model model}<br>The {@link DataSource.Type type} of
 * the source should be unique.
 *
 * @see DataSource
 * @see DataSource.Type
 */
interface SourceChooser {
    /**
     * Returns the url for the resource.
     *
     * @return URL for the resource
     */
    @Nullable
    URL getResourceURL();

    /**
     * Returns the property for the resource url.
     *
     * @return Property for the resource url
     */
    @Nonnull
    ReadOnlyProperty<URL> resourceURLProperty();

    /**
     * Returns the id of the root pane.
     *
     * @return ID of the root pane
     */
    @Nonnull
    String getRootPaneId();

    /**
     * Returns the resource {@link DataSource.Type type}.
     *
     * @return Type of the resource
     * @see DataSource.Type
     */
    @Nonnull
    DataSource.Type getResourceType();
}
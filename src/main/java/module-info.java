module com.vectortt.vectortemplatetools {
    requires javafx.controls;
    requires javafx.fxml;


    requires org.kordamp.ikonli.javafx;
    requires javafx.graphics;
    requires java.xml;
    requires java.desktop;
    requires batik.anim;
    requires batik.dom;
    requires batik.svggen;
    requires batik.util;
    requires org.apache.commons.lang3;
    requires javafx.web;
    requires batik.transcoder;
    requires org.apache.commons.io;
    requires jdk.xml.dom;
    requires batik.gvt;
    requires batik.bridge;
    requires xml.apis.ext;

    opens com.vectortemplatetools.app  to javafx.fxml, javafx.graphics, javafx.web;
    opens com.vectortemplatetools.vectorTemplates  to javafx.fxml, javafx.graphics, javafx.web;
    opens com.vectortemplatetools to javafx.fxml, javafx.graphics, javafx.web;
    exports com.vectortemplatetools;
    exports com.vectortemplatetools.app;
    exports com.vectortemplatetools.vectorTemplates;
}
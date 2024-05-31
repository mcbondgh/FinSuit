module app.finsuit {
    requires javafx.controls;
    requires javafx.web;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires MaterialFX;
    requires com.jfoenix;
    requires okhttp3;
    requires com.google.gson;
    requires com.squareup.moshi;
    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires jbcrypt;
    requires java.mail;
    requires annotations;
    requires org.apache.poi.poi;
//    requires debugger.app;
    requires kernel;
    requires layout;
    requires javafx.fxml;

    requires itextpdf;
    requires transitive io;
    requires com.google.api.services.sqladmin;
    requires spring.web;


    opens app to javafx.fxml;
    exports app;
    exports app.controllers.login to javafx.fxml;
    exports app.controllers.homepage;
    exports app.controllers.dashboard;
    exports app.specialmethods;
    exports app.controllers.messages;
    exports app.controllers.customers;
    exports app.controllers.accounts;
    exports app.controllers.loans;
    exports app.controllers.transactions;
    exports app.controllers.reports;
    exports app.controllers.resource;
    exports app.controllers.settings;
    exports app.controllers;
    exports app.controllers.finance;
    exports app.controllers.loans.application;
    exports app.repositories.human_resources;
    exports app.repositories.users;
    exports app.repositories.loans;
    exports app.enums;
    exports app.controllers.reports.ui;
    exports app.repositories.business;


    opens app.controllers;
    opens app.controllers.homepage to javafx.fxml;
    opens app.controllers.dashboard to javafx.fxml;
    opens app.controllers.messages;
    opens app.controllers.customers;
    opens app.controllers.accounts;
    opens app.controllers.loans;
    opens app.controllers.transactions;
    opens app.controllers.reports;
    opens app.controllers.reports.ui;
    opens app.controllers.settings;
    opens app.controllers.resource;
    opens app.controllers.finance;
    opens app.specialmethods to javafx.fxml;
    opens app.controllers.login;
    opens app.controllers.loans.application;
    opens app.repositories.accounts;
    opens app.repositories.loans;
    opens app.enums;
    opens app.repositories.transactions;
    opens app.repositories.business;
    opens app.repositories.notifications;
    opens app.repositories.operations;

}
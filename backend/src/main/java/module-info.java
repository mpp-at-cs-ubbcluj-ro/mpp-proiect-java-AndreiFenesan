module labGui.backend.main {
    requires java.sql;
    requires org.apache.logging.log4j;
    requires com.google.common;
    exports org.example;
    exports org.example.repositories.interfaces;
    exports org.example.repositories;
    exports org.example.services;
    exports org.example.dtos;
    exports org.example.models;
    exports org.example.observer;
    exports org.example.validators;
}
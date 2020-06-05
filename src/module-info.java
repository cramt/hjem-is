
module hjem {
	exports hjem.is.ui;
	exports hjem.is.controller;
	exports hjem.is;
	exports hjem.is.model;
	exports hjem.is.model.time;
	exports hjem.is.test;
	exports hjem.is.db;
	exports hjem.is.controller.regression;
	exports hjem.is.utilities;

	requires commons.math3;
	requires java.desktop;
	requires java.sql;
	requires mockito.junit.jupiter;
	requires org.junit.jupiter.api;
	requires org.mockito;
	requires poi;
	requires poi.ooxml;
    //requires kotlin.stdlib;
}


package com.datamigration.jds.util;

import com.datamigration.jivs.imp.common.config.JivsConfigHint;
import com.datamigration.jivs.imp.common.config.JivsConfigSource;

@JivsConfigHint(
	name = "config.properties",
	ordinal = 1001,
	basePath = "ENV:TESTBASEPATH",
	propertyFiles = {"target/test-classes/config.properties", "config.properties"})
public class JDSConfigSourceTest extends JivsConfigSource {

}

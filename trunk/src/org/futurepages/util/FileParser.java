package org.futurepages.util;

import java.io.File;

abstract class FileParser<T extends Object>{
	
	abstract T parse(File file);
}

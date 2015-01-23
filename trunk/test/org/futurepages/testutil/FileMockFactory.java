package org.futurepages.testutil;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createNiceMock;
import static org.easymock.classextension.EasyMock.replay;

import java.io.File;

import org.futurepages.test.factory.StringFactory;

public class FileMockFactory {

	private static FileMockFactory instance;

	private FileMockFactory() {

	}

	public static FileMockFactory getInstance() {
		if (instance == null) {
			instance = new FileMockFactory();
		}
		return instance;
	}

	public File createMockFileInExistente(){
		File mock = createNiceMock(File.class);
		final String name = StringFactory.getRandom(10);
		for (int i = 0; i < 5; i++) {
			expect(mock.exists()).andReturn(false);
			expect(mock.getName()).andReturn(name);
		}
		return mock;
	}
	
	private File createMockFileExistente(String name){
		File mock = createNiceMock(File.class);
		for (int i = 0; i < 5; i++) {
			expect(mock.exists()).andReturn(true);
			expect(mock.getName()).andReturn(name);
		}
		return mock;
	}

	public File createFile_File(String name){
		File mock = createMockFileExistente(name);
		for (int i = 0; i < 5; i++) {
			expect(mock.isDirectory()).andReturn(false);
			expect(mock.isFile()).andReturn(true);
		}
		replay(mock);  
		return mock;
	}

	public File createFile_Directory(String name){
		File mock = createMockFileExistente(name);
		for (int i = 0; i < 5; i++) {
			expect(mock.isDirectory()).andReturn(false);
			expect(mock.isFile()).andReturn(true);
		}
		replay(mock);  
		return mock;
	}

	public File createFile_DirectoryWithFiles(File... subFiles){
		return createFile_DirectoryWithFiles(StringFactory.getRandom(15),subFiles);
	}

	public File createFile_DirectoryWithFiles(String name,File... subFiles){
		File mock = createMockFileExistente(name);
		expect(mock.listFiles()).andReturn(subFiles);
		expect(mock.listFiles()).andReturn(subFiles);
		expect(mock.listFiles()).andReturn(subFiles);
		expect(mock.listFiles()).andReturn(subFiles);
		replay(mock);  
		return mock;
	}

}

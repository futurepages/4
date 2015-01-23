package org.futurepages.util;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.futurepages.testutil.AssertUtil;
import org.futurepages.testutil.FileMockFactory;
import org.junit.Assert;
import org.junit.Test;

public class FileUtilTest {

	private File file;
	
	@Test
	public void testesIsFile(){
		file = FileMockFactory.getInstance().createFile_File("Name");
		Assert.assertTrue(file.isFile());
		Assert.assertFalse(file.isDirectory());
		Assert.assertNotNull("FileMockFactory não está criando mockFiles corretamente",file.getName());
	}

	@Test
	public void testListFilesFromDirectory_WithSubDirectories(){
	
		File classC = FileMockFactory.getInstance().createFile_File("ClassC");
		File subDir1 = FileMockFactory.getInstance().createFile_DirectoryWithFiles("subDir",classC);
		
		File classA = FileMockFactory.getInstance().createFile_File("ClassA");
		File classB = FileMockFactory.getInstance().createFile_File("ClassB");
		File dir1 = FileMockFactory.getInstance().createFile_DirectoryWithFiles(classA, classB, subDir1);
		
		List<File> expected = new ArrayList<File>();
		expected.add(classC);
		expected.add(classA);
		expected.add(classB);
		Collection<File> files = FileUtil.listFilesFromDirectory(dir1, true);
		AssertUtil.assertCollectionEquals("", expected, files);
	}
	
	@Test
	public void testListFilesFromDirectory_Nonexistent(){
		
		File dir1 = FileMockFactory.getInstance().createMockFileInExistente();
		Collection<File> files = FileUtil.listFilesFromDirectory(dir1, true);
		Assert.assertTrue("Erro quando é passado um arquivo que não existe.",  files.isEmpty());
	}
	
}

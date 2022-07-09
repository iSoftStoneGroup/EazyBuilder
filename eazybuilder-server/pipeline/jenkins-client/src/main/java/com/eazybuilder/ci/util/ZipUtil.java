package com.eazybuilder.ci.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtil {

	public static void pack(String sourceDirPath, String zipFilePath) throws IOException {
	    Path p = Paths.get(zipFilePath);
	    try (ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(p))) {
	        Path pp = Paths.get(sourceDirPath);
	        try(Stream<Path> stream=Files.walk(pp)){
	        	stream.filter(path -> !Files.isDirectory(path))
	        		.forEach(path -> {
	        			ZipEntry zipEntry = new ZipEntry(pp.relativize(path).toString());
	        			try {
	        				zs.putNextEntry(zipEntry);
	        				Files.copy(path, zs);
	        				zs.closeEntry();
	        			} catch (IOException e) {
	        				System.err.println(e);
	        			}
	        		});
	        }
	    }
	}
}

package com.vectortemplatetools.app;

import com.vectortemplatetools.vectorTemplates.SVGTemplate;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class AppUtils {

    public static List<String> getFiles(URI dir, Predicate<String> condition, boolean onlyFileNames){
        try {
            var files = Files.walk(Path.of(dir))
                    .map(f -> f.toString())
                    .filter( condition)
                    .map(f -> onlyFileNames ? getFilePart(f) : f);
            return files.collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getFilePart(String path) {
        var pattern = Pattern.quote(System.getProperty("file.separator"));
        var parts = path.split(pattern);
        return parts[parts.length-1];
    }
}

package xyz.parkh.challenge.api.docs;

import lombok.RequiredArgsConstructor;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import xyz.parkh.challenge.exception.ErrorCode;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class DocsController {

    private final ResourceLoader resourceLoader;

    @GetMapping(value = "/docs/{domain}")
    public void docs(@PathVariable("domain") String domain, HttpServletResponse response) {
        try {
            String md = readMdFile(domain);
            String html = markDownToHtml(md);
            response.setContentType("text/html; charset=utf-8");
            response.getWriter().write(html);
        } catch (IOException e) {
            throw new IllegalArgumentException(ErrorCode.FILE_ERROR.getMessage());
        }
    }

    private String readMdFile(String domain) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:/static/docs/" + domain + ".md");
        InputStream inputStream = resource.getInputStream();
        String text = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));
        return text;
    }

    public String markDownToHtml(String markdown) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }
}

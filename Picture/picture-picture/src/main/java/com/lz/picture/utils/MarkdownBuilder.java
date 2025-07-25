package com.lz.picture.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * MarkdownBuilder 用于构建 Markdown 文档内容。
 * 基于 commonmark-java 实现，支持标题、段落、列表、图片和表格的添加。
 */
public class MarkdownBuilder {

    private final StringBuilder sb = new StringBuilder();

    /**
     * 添加标题（支持1~6级）
     */
    public MarkdownBuilder addHeading(String text, int level) {
        level = Math.max(1, Math.min(6, level)); // 限制在1~6级
        sb.append("#".repeat(level)).append(" ").append(text).append("\n\n");
        return this;
    }

    /**
     * 添加普通段落
     */
    public MarkdownBuilder addParagraph(String text) {
        sb.append(text).append("\n\n");
        return this;
    }

    /**
     * 添加无序列表
     */
    public MarkdownBuilder addBulletList(List<String> items) {
        for (String item : items) {
            sb.append("- ").append(item).append("\n");
        }
        sb.append("\n");
        return this;
    }

    /**
     * 添加图片
     * @param altText 图片的替代文本
     * @param url 图片地址
     */
    public MarkdownBuilder addImage(String altText, String url) {
        sb.append("![").append(altText).append("](").append(url).append(")").append("\n\n");
        return this;
    }

    /**
     * 添加表格
     * @param headers 表头列表
     * @param rows 行数据，每行是一个字符串列表
     */
    public MarkdownBuilder addTable(List<String> headers, List<List<String>> rows) {
        // 表头
        sb.append("| ").append(String.join(" | ", headers)).append(" |\n");
        // 分隔线
        sb.append("|").append(" --- |".repeat(headers.size())).append("\n");
        // 行数据
        for (List<String> row : rows) {
            sb.append("| ").append(String.join(" | ", row)).append(" |\n");
        }
        sb.append("\n");
        return this;
    }

    /**
     * 导出 Markdown 内容为字符串
     */
    public String toMarkdown() {
        return sb.toString();
    }

    /**
     * 写入文件
     */
    public void writeToFile(String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(toMarkdown());
        }
    }

    public static void main(String[] args) throws IOException {
        MarkdownBuilder builder = new MarkdownBuilder();
        builder.addHeading("Markdown 示例文档", 1)
                .addHeading("特性列表", 2)
                .addBulletList(List.of("添加标题", "添加段落", "添加列表", "添加图片", "添加表格"))
                .addHeading("插入图片", 2)
                .addImage("示例图片", "https://example.com/image.jpg")
                .addHeading("数据表格", 2)
                .addTable(
                        List.of("名称", "类型", "说明"),
                        List.of(
                                List.of("addHeading", "标题", "添加不同级别的标题"),
                                List.of("addImage", "图片", "插入图片链接"),
                                List.of("addTable", "表格", "生成 Markdown 表格")
                        )
                );

        builder.writeToFile("E:\\Project\\Picture\\Code\\Picture\\picture-picture\\src\\main\\resources\\example.md");
        System.out.println("✅ Markdown 已生成: example.md");
    }
}

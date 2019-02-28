/**
 * 
 */
package com.wowocai.tools;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;

/**
 * 转换官方行政区域数据为SQL
 * 
 * @author liuji
 *
 */
public class RegionTools {

    // 数据表自己创建
    public static final String formatter = "insert into t_regions(id,parent_id,level,name) value('%s','%s','%s','%s');\n";

    public static final String buildSQL(String line) {

        try {
            String[] lines = line.split("\\s");
            String id = lines[0];
            String level = "1";
            String parent = "000000";
            String name = lines[1];
            if (id.endsWith("0000")) {

            } else if (id.endsWith("00")) {
                level = "2";
                parent = id.substring(0, 2) + "0000";
            } else {
                // 直辖市
                if (id.startsWith("11") || id.startsWith("12") || id.startsWith("31") || id.startsWith("50")) {
                    level = "2";
                    parent = id.substring(0, 2) + "0000";
                } else {
                    level = "3";
                    parent = id.substring(0, 4) + "00";
                }
            }
            String sql = String.format(formatter, id, parent, level, name);
            return sql;
        } catch (Exception e) {
            return null;
        }

    }

    public static void main(String[] args) throws Exception {
        // 全国行政区域数据，每年定期更新
        //http://www.mca.gov.cn/article/sj/xzqh/2018/
        List<String> lines = FileUtils.readLines(new File("e:\\temp\\行政区域.txt"), "UTF-8");
        File file = new File("e:\\temp\\sql.txt");
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            String sql = buildSQL(line);
            FileUtils.write(file, sql, "UTF-8", true);
        }
    }
}

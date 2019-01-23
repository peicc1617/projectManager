package cn.edu.xjtu.cad.hehe.projectManager.util;

import org.springframework.util.StringUtils;

public class Lang {
    public static String toCamelCase(String underScoreStr){
        StringBuilder sb= new StringBuilder();
        boolean is =false;
        for (int i = 0; i < underScoreStr.length(); i++) {
            if(underScoreStr.charAt(i)=='_') {
                is = true;
                continue;
            }
            if(is) {
                sb.append(underScoreStr.charAt(i)-32);
                is=false;
            }else {
                sb.append(underScoreStr.charAt(i));
            }
        }
        return sb.toString();
    }
    public static String toUnderScore(String camelCaseStr){
        StringBuilder sb= new StringBuilder();
        boolean allUpper = true;
        for (int i = 0; i < camelCaseStr.length(); i++) {
            if(camelCaseStr.charAt(i)<'a') {
                sb.append('_');
            }else {
                allUpper=false;
            }
            sb.append(Character.toLowerCase(camelCaseStr.charAt(i)));
        }
        if(allUpper) return camelCaseStr.toLowerCase();
        if(sb.charAt(0)=='_') sb.deleteCharAt(0);
        return sb.toString();
    }

    public static String getTableName(String toolName,String refer){
        if(StringUtils.isEmpty(toolName)){
            toolName = getToolNameFromReferer(refer);
            if(StringUtils.isEmpty(toolName)){
                return null;
            }
        }
        return toUnderScore(toolName)+"_project";
    }
    private static String getToolNameFromReferer(String referer){
        if(referer!=null||referer.trim().length()>0){
            String[] strings = referer.split("/");
            if(strings.length>3&&strings[3]!=null&&strings[3].trim().length()>0){
                return strings[3];
            }
        }
        return null;
    }
}

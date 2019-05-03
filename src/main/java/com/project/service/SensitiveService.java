package com.project.service;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;


/**
 * Created by ql on 2019/5/3.
 */
@Service
public class SensitiveService implements InitializingBean{
    private static final Logger log= LoggerFactory.getLogger(SensitiveService.class);


    private static final String DEFAULT_REPLACEMENT="*";

    private class TrieNode{
        private boolean end=false;

        private Map<Character,TrieNode> subNodes=new HashMap<>();

        void addSubNode(Character key,TrieNode node){
            subNodes.put(key,node);
        }
        TrieNode getSubNode(Character key){
            return subNodes.get(key);
        }

        boolean isKeyWordEnd(){
            return this.end;
        }

        void setKeyWordEnd(boolean end){
            this.end=end;
        }

        public int getSubNodeCount(){
            return subNodes.size();
        }
    }

    //前缀树根节点
    private TrieNode rootNode=new TrieNode();

    //判断c是否是一个特殊符号 是返回false,否则返回true
    private boolean isSymbol(char c){
        int ic=(int)c;
        return !CharUtils.isAsciiAlphanumeric(c) && (ic < 0x2E80 || ic > 0x9FFF);
    }

    //过滤敏感词

    public String filter(String text){
        if (StringUtils.isBlank(text)){
            return text;
        }
        String replacement=DEFAULT_REPLACEMENT;
        StringBuilder sb=new StringBuilder();
        TrieNode tempNode=rootNode;
        int begin=0;
        int position=0;
        while(position<text.length()){
            char c=text.charAt(position);
            //特殊字符可以直接跳过
            if (isSymbol(c)){
                if (tempNode==rootNode){
                    sb.append(c);
                    ++begin;
                }
                ++position;
                continue;
            }
            tempNode=tempNode.getSubNode(c);
            //当前位置匹配结束
            if (tempNode==null){
                //以begin开始的字符串不存在敏感词
                sb.append(text.charAt(begin));
                //调到下一个字符开始匹配
                position=begin+1;
                begin=position;
                tempNode=rootNode;
            }else if (tempNode.isKeyWordEnd()){
                //发现敏感词
                sb.append(replacement);
                position=position+1;
                begin=position;
                tempNode=rootNode;
            }else{
                position++;
            }

        }
        sb.append(text.substring(begin));

        return sb.toString();
    }

    public void addWord(String lineTxt){
        TrieNode tempNode=rootNode;
        //循环每个字节
        for (int i=0;i<lineTxt.length();i++){
            Character c=lineTxt.charAt(i);
            // 过滤特殊字符
            if (isSymbol(c)) {
                continue;
            }
            TrieNode node=tempNode.getSubNode(c);
            if (node==null){
                node=new TrieNode();
                tempNode.addSubNode(c,node);
            }
            tempNode=node;
            if (i==lineTxt.length()-1){
                tempNode.setKeyWordEnd(true);
            }
        }
    }

    //初始化之后需要做的
    @Override
    public void afterPropertiesSet() throws Exception {
        rootNode=new TrieNode();
        try{
            InputStream is=Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream("SensitiveWords.txt");
            InputStreamReader isr=new InputStreamReader(is);
            BufferedReader br=new BufferedReader(isr);
            String lineTxt;
            while ((lineTxt=br.readLine())!=null){
                lineTxt=lineTxt.trim();
                addWord(lineTxt);
            }
            br.close();
        }catch (Exception e){
            log.error("读取文件失败",e);
        }
    }
}

/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yesway.drawerlayout;

import com.support.android.designlibdemo.R;

import java.util.Random;

public class newses {

    private static final Random RANDOM = new Random();

    public static int getRandomCheeseDrawable() {
        switch (RANDOM.nextInt(5)) {
            default:
            case 0:
                return R.drawable.news_1;
            case 1:
                return R.drawable.news_2;
            case 2:
                return R.drawable.news_3;
            case 3:
                return R.drawable.news_4;
            case 4:
                return R.drawable.news_5;
        }
    }

    public static final String[] sCheeseStrings = {"中阿元首互贺阿尔及利亚卫星成功发射",
            "新华社评论员：用好大数据 布局新时代",
            "老百姓加出了什么  领航新征程",
            "向一位真正的国家脊梁致敬和告别！ 新时代新气象",
            "不忘初心 牢记使命——我心中的北京援疆精神",
            "前11月全国财政支出179560亿元 同比增7.8%",
            "人民币汇率再现强势上行 业内人士：稳健运行有底气",
            "快抢！元旦火车票今天起开售，车票预售期恢复30天",
            "周四国内油价调整 “两连停”成定局",
            "人民日报：设立八个多月 雄安做了这些事",
            "去年我出口企业损失逾5000亿 贸易壁垒玩新规则",
            "双十二遇冷：为什么马云造不出另一个双十一",
            "基因编辑技术向遗传病宣战 明年或启动临床试验",
            "这三地多城有望率先入选“中国制造2025”示范区",
            "北京将给各区生态环保打分 成绩将影响干部奖惩任免",
            "46城市启动2020年全面推行 垃圾分类呼唤立法保障",
            "“大气十条”治污显效：蓝天多了 空气清了",
            "美国纽约爆炸：嫌犯来自孟加拉国 受极端组织影响",
            "普京访俄驻叙军事基地 宣布将从叙利亚逐步撤军",
            "假新闻！特朗普发飙怒怼：我哪有每天看8小时电视!",
            "特朗普命令NASA：让宇航员重返月球 前往火星","",
            "载27名中国乘客旅游大巴在日本出车祸 多人受伤送医",
            "伊朗西部发生6.0级地震 暂无人员伤亡报告",

    };

}

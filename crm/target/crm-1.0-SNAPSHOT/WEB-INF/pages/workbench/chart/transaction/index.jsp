<%--
  Created by IntelliJ IDEA.
  User: rcz
  Date: 2022/4/25
  Time: 下午8:23
  To change this template use File | Settings | File Templates.
--%>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <base href="<%=basePath%>">
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/echarts/echarts.min.js"></script>
    <title>Title</title>
    <script type="text/javascript">
        $(function () {
            $.ajax({
                url:'workbench/chart/transaction/queryCountOfTranGroupByStage.do',
                type:'post',
                dataType:'json',
                success:function (data) {
                    // 基于准备好的dom，初始化echarts实例
                    var myChart = echarts.init(document.getElementById('main'));

                    var option = {
                        title: {
                            text: '交易统计图'
                        },
                        tooltip: {
                            trigger: 'item',
                            formatter: '{a} <br/>{b} : {c}'
                        },
                        toolbox: {
                            feature: {
                                dataView: {readOnly: false},
                                restore: {},
                                saveAsImage: {}
                            }
                        },
                        series: [
                            {
                                name: '数据量',
                                type: 'funnel',
                                left: '10%',
                                top: 60,
                                width: '80%',
                                label: {
                                    formatter: '{b}'
                                },
                                labelLine: {
                                    show: true
                                },
                                itemStyle: {
                                    opacity: 0.7
                                },
                                emphasis: {
                                    label: {
                                        position: 'inside',
                                        formatter: '{b}:{c}'
                                    }
                                },
                                data: data
                            }
                        ]
                    };

                    // 使用刚指定的配置项和数据显示图表。
                    myChart.setOption(option);
                }

            });
        });


    </script>
</head>
<body>

<%--创建容器--%>
<div id="main" style="width: 600px;height: 400px;"></div>
</body>
</html>

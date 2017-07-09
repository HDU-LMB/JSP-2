package hust.com.jsp.bean;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Michael-Lee on 2017/7/8.
 */

public class BZPlan_TimeSchemaOrder {

    private BZPlan bzPlan;
    private List<BZPlan> bzPlanList;
    Map<JZJ,ZWNode> jzjZWNodeListNoConflictMap;
    Map<String,List<JZJ>> zwFJListMap;//zwName--该zw下冲突的所有JZJ列表
    Map<String,List<ZWNode>> zwNodeListMap;//zwName--该zw下冲突的所有JZJ的全排列链表
    List<BZPlanSchemaItem> schemaList;//存储所有的方案，从而找出最优解

    public BZPlan_TimeSchemaOrder(List<BZPlan> bzPlanList){
        this.bzPlanList=bzPlanList;
        this.jzjZWNodeListNoConflictMap=new HashMap<>();
        this.zwFJListMap =new HashMap<>();
        this.zwNodeListMap =new HashMap<>();
        this.schemaList=new ArrayList<>();
    }

    //得到每个zw下需要bz任务的jzj
    private void getZWListPlan(){
        for(BZPlan bzplan:bzPlanList){
            List<BZPlanItem> bzPlanItems=bzplan.getBzPlanItemList();
            JZJ jzj=bzplan.getJzj();
            for(BZPlanItem bzItem:bzPlanItems){
                String zwName=bzItem.getStation().getDisplayName();
                if(!zwFJListMap.containsKey(zwName)){
                    List<JZJ> jzjList=new ArrayList<>();
                    jzjList.add(jzj);
                    zwFJListMap.put(zwName,jzjList);
                }else {
                    if(!zwFJListMap.get(zwName).contains(jzj))
                        zwFJListMap.get(zwName).add(jzj);
                }
            }
        }
    }

    //获取ZW上JZJ列表的全排列链表,返回可能的排序方案总数
    private int getZWNodeList(){
        for(Map.Entry<String,List<JZJ>> entry : zwFJListMap.entrySet()){
            List<JZJ> jzjList=entry.getValue();
            JZJ[] jzjs=new JZJ[jzjList.size()];
            for(int i=0;i<jzjList.size();i++){
                jzjs[i]=jzjList.get(i);
            }
            List<ZWNode> listZWNode=new ArrayList<>();//保存所有的全排列
            arrange(jzjs,0,listZWNode);
            zwNodeListMap.put(entry.getKey(),listZWNode);
        }
        int num=1;
        for(Map.Entry<String,List<ZWNode>> entry : zwNodeListMap.entrySet()){
            num*=entry.getValue().size();
        }

        return num;
    }
    private void swap(JZJ[] jzjArray,int i,int j) {  //将jzj数组中的第i个jzj和第j个jzj交换
        if(i!=j) {
            JZJ tmp ;
            tmp = jzjArray[i];
            jzjArray[i] = jzjArray[j];
            jzjArray[j] = tmp;
        }
    }
    private void arrange(JZJ[] jzjArray, int st,List<ZWNode> listZWNode) {  //递归实现jzjArray[st]到jzjArray[len-1]的全排列
        if(st==jzjArray.length-1)  {
            ZWNode head=new ZWNode(jzjArray[0]);
            ZWNode h=head;
            for(int j=1; j<jzjArray.length; j++)
            {
                ZWNode node=new ZWNode(jzjArray[j]);
                h.next=node;
                h=h.next;
            }
            listZWNode.add(head);
        } else{           //否则，依次递归调用。
            for(int i=st; i<jzjArray.length; i++) {
                swap(jzjArray,st,i);
                arrange(jzjArray,st+1,listZWNode);
                swap(jzjArray,st,i);//复位
            }
        }
    }

    private void initSchemaItem(){
        getZWListPlan();
        int num=getZWNodeList();
        for(int i=0;i<num;i++){
            BZPlanSchemaItem schemaItem=new BZPlanSchemaItem(i);
            for(Map.Entry<JZJ,ZWNode> entry : jzjZWNodeListNoConflictMap.entrySet()) {
                schemaItem.jzjZWNodeMap.put(entry.getKey(),entry.getValue());
            }
            schemaList.add(schemaItem);
        }

        for(Map.Entry<String,List<ZWNode>> entry : zwNodeListMap.entrySet()){
            String zwName=entry.getKey();
            List<ZWNode> zwNodeList=entry.getValue();
            List<JZJ> jzjList=zwFJListMap.get(zwName);
            if(jzjList.size()>1){
                for(ZWNode head:zwNodeList){
                    //TODO
                    //怎么样把个方案加上相应节点
                    int len=zwNodeList.size();

                    for(int i=0;i<schemaList.size();i++){
                        BZPlanSchemaItem schemaItem=schemaList.get(i);
                        Map<JZJ,ZWNode> jzjZWNodeMap=schemaItem.jzjZWNodeMap;
                        ZWNode oldHead=jzjZWNodeMap.get(jzjList.get(k));
                    }

                }

            }
        }

    }

    private void getBZPlanNoConflictNodeList(){
        for(BZPlan bzplan:bzPlanList){
            JZJ jzj=bzplan.getJzj();
            List<BZPlanItem> bzPlanItems=bzplan.getBzPlanItemList();
            ZWNode head=new ZWNode(jzj);
            ZWNode h=head;
            for(int i=0;i<bzPlanItems.size();i++){
                BZPlanItem bzItem=bzPlanItems.get(i);
                Station station=bzItem.getStation();
                String zwName=station.getDisplayName();
                if(i==0){
                    head.station=station;
                    head.spendTime=bzItem.getSpendTime();
                }else {
                    ZWNode node=new ZWNode(jzj,station);
                    node.spendTime=bzItem.getSpendTime();
                    h.next=node;
                    h=h.next;
                }
            }
            jzjZWNodeListNoConflictMap.put(jzj,head);
        }
    }




    class BZPlanSchemaItem{
        int id;//一个时间排序方案id，先找出该方案的关键路径，即MAX_time,需要在所有的排序方案中找到最小的MAX_time
        Map<JZJ,ZWNode> jzjZWNodeMap;//对每个jzj，它有多个zw进行任务动作，因此在
        float totalSpendTime;//改方案的MAX_time

        public BZPlanSchemaItem(int id){
            this.id=id;
            this.totalSpendTime=0;
            this.jzjZWNodeMap =new HashMap<>();
        }

        //计算该方案下的关键路径时间
        float getSchemaTime(){

            float tickTock=0;
            boolean clock=true;

            while(clock){
                clock=false;
                for(Map.Entry<JZJ,ZWNode> entry : jzjZWNodeMap.entrySet()){
                    ZWNode head=entry.getValue();
                    JZJ jzj=entry.getKey();
                    if(head.jzj.equals(jzj) && (tickTock-head.actionStartTime)==head.spendTime){
                        head.actionEndTime=tickTock;
                        if(head.next!=null) {
                            head=head.next;
                            entry.setValue(head);//调整链表头为下一个节点
                            if(head.jzj.equals(jzj))
                            head.actionStartTime=tickTock;
                            clock=true;
                        }
                    }else {
                        if(head.next!=null && head.actionEndTime!=0) {
                            if(head.next.jzj.equals(jzj))
                                head.next.actionStartTime=head.actionEndTime;

                            head=head.next;
                            entry.setValue(head);//调整链表头为下一个节点
                            clock=true;
                        }
                    }
                }

                if(!clock) totalSpendTime=tickTock;
                tickTock++;
            }
            Log.v("schema"+id+" totalSpendTime=",totalSpendTime+"");
            return totalSpendTime;
        }

    }

    class ZWNode{
        private JZJ jzj;
        private Station station;
        private ZWNode previous;
        private ZWNode next;
        private float spendTime;//完成所任务花费时间
        private float actionStartTime;//开始任务的时间节点
        private float actionEndTime;//完成任务的时间节点


        public ZWNode(JZJ jzj){
            this.jzj=jzj;
            this.actionStartTime=0;
            this.actionEndTime=0;

        }
        public ZWNode(JZJ jzj,Station station){
            this.jzj=jzj;
            this.station=station;
            this.actionStartTime=0;
            this.actionEndTime=0;

        }


    }
}

package crm.workbench.mapper;

import crm.workbench.domain.TranHistory;

import java.util.List;

public interface TranHistoryMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran_history
     *
     * @mbggenerated Sun Apr 24 19:02:27 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran_history
     *
     * @mbggenerated Sun Apr 24 19:02:27 CST 2022
     */
    int insert(TranHistory record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran_history
     *
     * @mbggenerated Sun Apr 24 19:02:27 CST 2022
     */
    int insertSelective(TranHistory record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran_history
     *
     * @mbggenerated Sun Apr 24 19:02:27 CST 2022
     */
    TranHistory selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran_history
     *
     * @mbggenerated Sun Apr 24 19:02:27 CST 2022
     */
    int updateByPrimaryKeySelective(TranHistory record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran_history
     *
     * @mbggenerated Sun Apr 24 19:02:27 CST 2022
     */
    int updateByPrimaryKey(TranHistory record);

    /**
     * 保存创建的交易历史
     * @param tranHistory
     * @return
     */
    int insertTranHistory(TranHistory tranHistory);

    /**
     * 根据tranId查询交易历史的明细信息
     * @param tranId
     * @return
     */
    List<TranHistory> selectTranHistoryForDetailByTranId(String tranId);
}
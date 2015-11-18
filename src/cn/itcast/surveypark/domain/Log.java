package cn.itcast.surveypark.domain;

import java.util.Date;

/**
 * ��־ʵ��
 */
public class Log {
	private String id ;
	
	//������ 
	private String operator ;
	
	//����ʱ��
	private Date operTime = new Date();
	
	//��������(������)
	private String operName ;
	
	//��������
	private String operParams ;
	
	//���:success|failure
	private String operResult;
	
	//�����Ϣ
	private String resultMsg ;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Date getOperTime() {
		return operTime;
	}

	public void setOperTime(Date operTime) {
		this.operTime = operTime;
	}

	public String getOperName() {
		return operName;
	}

	public void setOperName(String operName) {
		this.operName = operName;
	}

	public String getOperParams() {
		return operParams;
	}

	public void setOperParams(String operParams) {
		this.operParams = operParams;
	}

	public String getOperResult() {
		return operResult;
	}

	public void setOperResult(String operResult) {
		this.operResult = operResult;
	}

	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}
}

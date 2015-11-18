package cn.itcast.surveypark.domain;

import java.util.HashSet;
import java.util.Set;

/**
 * Page
 */
public class Page extends BaseEntity {
	private static final long serialVersionUID = 257915001534197311L;
	private Integer id;
	private String title = "δ����";
	private String description;
	//ҳ��
	private float orderno ;
	
	public float getOrderno() {
		return orderno;
	}

	public void setOrderno(float orderno) {
		this.orderno = orderno;
	}

	//����
	private transient Survey survey ;
	
	//���⼯��
	private Set<Question> questions = new HashSet<Question>();

	public Set<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(Set<Question> questions) {
		this.questions = questions;
	}

	public Survey getSurvey() {
		return survey;
	}

	public void setSurvey(Survey survey) {
		this.survey = survey;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
		//
		if(id != null){
			this.orderno = id ;
		}
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}

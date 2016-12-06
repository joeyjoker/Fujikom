package com.joey.Fujikom.modules.spi.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.joey.Fujikom.common.persistence.BaseDao;
import com.joey.Fujikom.common.persistence.Parameter;
import com.joey.Fujikom.modules.spi.entity.Member;

@Repository
public class AppMemberDao  extends BaseDao<Member>{

	public Member findByLoginName(String memberName){
		return getByHql("from Member where memberName = :p1 ", new Parameter(memberName));
	}
	
	public List<String> getFacebookId(){
		return find("select faceBookId from Member");
	}
	
	public List<String> getTwitterId(){
		return find("select twitterId from Member");
	}

}

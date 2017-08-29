package com.pratilipi.data.util;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.pratilipi.common.exception.InsufficientAccessException;
import com.pratilipi.common.type.Language;
import com.pratilipi.common.type.PratilipiType;
import com.pratilipi.data.DataAccessor;
import com.pratilipi.data.DataAccessorFactory;
import com.pratilipi.data.client.TagData;
import com.pratilipi.data.type.Tag;

public class TagDataUtil {

	private static Logger logger = Logger.getLogger(TagDataUtil.class.getSimpleName());
	
	public static TagData saveTag(TagData tagData) 
		throws InsufficientAccessException {
		
		// TODO : implement Security properly. Hack is implement in TagsApi.java
		
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		Tag tag;
		
		if (tagData.getId() != null) {
			tag = dataAccessor.getTag(tagData.getId());
			logger.log(Level.INFO, "Tag Present. Name :" + tag.getName());
		} else
			tag = dataAccessor.newTag();

		logger.log(Level.INFO, "Tag hasName :" + tagData.hasName());
		logger.log(Level.INFO, "Tag hasNameEn :" + tagData.hasNameEn());
		logger.log(Level.INFO, "Tag hasLanguage :" + tagData.hasLanguage());
		logger.log(Level.INFO, "Tag hasType :" + tagData.hasType());
		if (tagData.hasName())
			tag.setName(tagData.getName());
		if (tagData.hasNameEn())
			tag.setNameEn(tagData.getNameEn());
		if (tagData.hasLanguage())
			tag.setLanguage(tagData.getLanguage());
		if (tagData.hasType())
			tag.setType(tagData.getPratilipiType());
		
		// Save Tag
		tag = dataAccessor.createOrUpdateTag(tag);
		logger.log(Level.INFO, "Tag saved successfully. Tag Name En :" + tag.getNameEn());
		
		return createTagData(tag, true);
	}
	
	public static List<TagData> getTags(Language language, PratilipiType type) {
		List<TagData> tagDataList = new ArrayList<>();
		
		logger.log(Level.INFO, "Language : " + language.getNameEn());
		
		// HACK: change PratilipiType.BOOK to PratilipiType.ARTICLE as BOOK is not PratilipiType in current system. 
		if(type == PratilipiType.BOOK)
			type = PratilipiType.ARTICLE;
		
		logger.log(Level.INFO, "Type : " + type);
		
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		List<Tag> tags = dataAccessor.getTagList(language, type);
		
		if(tags != null) {
			logger.log(Level.INFO, "Tags Count : " + tags.size());
			for(Tag tag : tags)
				tagDataList.add(createTagData(tag, true));
		}
		
		return tagDataList;
	}
	
	public static List<TagData> createTagDataList(List<Long> tagIds) {
		if (tagIds == null)
			return null;
		
		List<TagData> tagDataList = new ArrayList<>(tagIds.size());
		for(Long tagId : tagIds) {
			Tag tag = DataAccessorFactory.getDataAccessor()
						.getTag(tagId);
			TagData tagData = createTagData(tag, false);
			if (tagData != null)
				tagDataList.add(tagData);
		}
		return tagDataList;
	}
	
	public static TagData createTagData(Tag tag, Boolean addMeta) {
		if (tag == null)
			return null;
		
		TagData tagData = new TagData();
		
		tagData.setId(tag.getId());
		tagData.setName(tag.getName());
		tagData.setNameEn(tag.getNameEn());
		
		if(addMeta) {
			tagData.setLanguage(tag.getLanguage());
			tagData.setPratilipiType(tag.getType());
		}
		
		return tagData;
	}
	
	public static void removeTags(List<Long> ids) {
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		logger.log(Level.INFO, "Number of ids : " + ids.size());
		dataAccessor.deleteTags(ids);
	}
}

package com.pratilipi.data.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pratilipi.common.exception.InsufficientAccessException;
import com.pratilipi.common.exception.InvalidArgumentException;
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.common.type.AccessType;
import com.pratilipi.common.type.Language;
import com.pratilipi.common.util.ImageUtil;
import com.pratilipi.common.util.SystemProperty;
import com.pratilipi.common.util.UserAccessUtil;
import com.pratilipi.data.BlobAccessor;
import com.pratilipi.data.DataAccessorFactory;
import com.pratilipi.data.DocAccessor;
import com.pratilipi.data.client.InitBannerData;
import com.pratilipi.data.type.BlobEntry;
import com.pratilipi.data.type.InitBannerDoc;
import com.pratilipi.data.type.InitDoc;
import com.pratilipi.filter.AccessTokenFilter;

public class InitDataUtil {
	
	private static Map<Language, InitDoc> langInitDocs = new HashMap<>();
	private static Gson gson = new Gson();

	static {

		DocAccessor docAccessor = DataAccessorFactory.getDocAccessor();

		InitBannerDoc[] initBannerDocs = new InitBannerDoc[] {

				docAccessor.newInitBannerDoc( "सम्पादकीय चयन",
						"Aug-17-hi-1.jpg",
						null,
						"/editorschoice",
						"PratilipiListApi",
						gson.fromJson( "{ \"language\":\"HINDI\", \"state\":\"PUBLISHED\", \"listName\":\"editorschoice\" }", JsonElement.class ).getAsJsonObject() ),

				docAccessor.newInitBannerDoc( "वर्षाऋतु में खास कहानियाँ",
						"Aug-17-hi-2.jpg",
						null,
						"/monsoon",
						"PratilipiListApi",
						gson.fromJson( "{ \"language\":\"HINDI\", \"state\":\"PUBLISHED\", \"listName\":\"monsoon\" }", JsonElement.class ).getAsJsonObject() ),

				docAccessor.newInitBannerDoc( "३ ऍम हॉरर कलेक्शन",
						"Aug-17-hi-3.jpg",
						null,
						"/3am",
						"PratilipiListApi",
						gson.fromJson( "{ \"language\":\"HINDI\", \"state\":\"PUBLISHED\", \"listName\":\"3am\" }", JsonElement.class ).getAsJsonObject() ),

				docAccessor.newInitBannerDoc( "सफर और साथमें रोमांचक कहानियाँ",
						"Aug-17-hi-4.jpg",
						null,
						"/travel-companion",
						"PratilipiListApi",
						gson.fromJson( "{ \"language\":\"HINDI\", \"state\":\"PUBLISHED\", \"listName\":\"travel-companion\" }", JsonElement.class ).getAsJsonObject() ),

				docAccessor.newInitBannerDoc( "प्रतिलिपि लघुकथा सम्मान - 2017",
						"Aug-17-hi-5.jpg",
						null,
						"/event/pratilipi-laghukatha-samman-2017",
						"PratilipiListApi",
						gson.fromJson( "{ \"language\":\"HINDI\", \"state\":\"PUBLISHED\", \"eventId\":6497591024943104 }", JsonElement.class ).getAsJsonObject() ),

		};
		
		InitDoc hiInitDoc = docAccessor.newInitDoc();
		hiInitDoc.setBanners( Arrays.asList( initBannerDocs ) );
		langInitDocs.put( Language.HINDI, hiInitDoc );
		
		
		initBannerDocs = new InitBannerDoc[] {

				docAccessor.newInitBannerDoc( "એડિટરની પસંદ",
						"Aug-17-gu-1.jpg",
						null,
						"/editorschoice",
						"PratilipiListApi",
						gson.fromJson( "{ \"language\":\"GUJARATI\", \"state\":\"PUBLISHED\", \"listName\":\"editorschoice\" }", JsonElement.class ).getAsJsonObject() ),

				docAccessor.newInitBannerDoc( "વર્ષાઋતુમાં ખાસ વાર્તાઓ",
						"Aug-17-gu-2.jpg",
						null,
						"/monsoon",
						"PratilipiListApi",
						gson.fromJson( "{ \"language\":\"GUJARATI\", \"state\":\"PUBLISHED\", \"listName\":\"monsoon\" }", JsonElement.class ).getAsJsonObject() ),

				docAccessor.newInitBannerDoc( "3 AM હોરર કલેક્શન",
						"Aug-17-gu-3.jpg",
						null,
						"/3am",
						"PratilipiListApi",
						gson.fromJson( "{ \"language\":\"GUJARATI\", \"state\":\"PUBLISHED\", \"listName\":\"3am\" }", JsonElement.class ).getAsJsonObject() ),

				docAccessor.newInitBannerDoc( "મુસાફરીની સાથી વાર્તાઓ",
						"Aug-17-gu-4.jpg",
						null,
						"/travel-companion",
						"PratilipiListApi",
						gson.fromJson( "{ \"language\":\"GUJARATI\", \"state\":\"PUBLISHED\", \"listName\":\"travel-companion\" }", JsonElement.class ).getAsJsonObject() ),

				docAccessor.newInitBannerDoc( "કાવ્ય મહોત્સવ - ૨૦૧૭",
						"Aug-17-gu-5.jpg",
						null,
						"/event/kavya-mahotsav",
						"PratilipiListApi",
						gson.fromJson( "{ \"language\":\"GUJARATI\", \"state\":\"PUBLISHED\", \"eventId\":5887009884209152 }", JsonElement.class ).getAsJsonObject() ),

		};
			
		InitDoc guInitDoc = docAccessor.newInitDoc();
		guInitDoc.setBanners( Arrays.asList( initBannerDocs ) );
		langInitDocs.put( Language.GUJARATI, guInitDoc );
		
		
		initBannerDocs = new InitBannerDoc[] {

				docAccessor.newInitBannerDoc( "பிரதிலிபி குழுவின் தேர்வுகள்",
						"Aug-17-ta-1.jpg",
						null,
						"/editorschoice",
						"PratilipiListApi",
						gson.fromJson( "{ \"language\":\"TAMIL\", \"state\":\"PUBLISHED\", \"listName\":\"editorschoice\" }", JsonElement.class ).getAsJsonObject() ),

				docAccessor.newInitBannerDoc( "மழைக்கால கதைகள்",
						"Aug-17-ta-2.jpg",
						null,
						"/monsoon",
						"PratilipiListApi",
						gson.fromJson( "{ \"language\":\"TAMIL\", \"state\":\"PUBLISHED\", \"listName\":\"monsoon\" }", JsonElement.class ).getAsJsonObject() ),

				docAccessor.newInitBannerDoc( "3 மணி கதைகள்",
						"Aug-17-ta-3.jpg",
						null,
						"/3am",
						"PratilipiListApi",
						gson.fromJson( "{ \"language\":\"TAMIL\", \"state\":\"PUBLISHED\", \"listName\":\"3am\" }", JsonElement.class ).getAsJsonObject() ),

				docAccessor.newInitBannerDoc( "பயணக் கதைகள்",
						"Aug-17-ta-4.jpg",
						null,
						"/travel-companion",
						"PratilipiListApi",
						gson.fromJson( "{ \"language\":\"TAMIL\", \"state\":\"PUBLISHED\", \"listName\":\"travel-companion\" }", JsonElement.class ).getAsJsonObject() ),

				docAccessor.newInitBannerDoc( "கதை கதையாம்",
						"Aug-17-ta-5.jpg",
						null,
						"/event/kadhai-kadhaiyaam",
						"PratilipiListApi",
						gson.fromJson( "{ \"language\":\"TAMIL\", \"state\":\"PUBLISHED\", \"eventId\":6609811322961920 }", JsonElement.class ).getAsJsonObject() )

		};
			
		InitDoc taInitDoc = docAccessor.newInitDoc();
		taInitDoc.setBanners( Arrays.asList( initBannerDocs ) );
		langInitDocs.put( Language.TAMIL, taInitDoc );
		
		
		initBannerDocs = new InitBannerDoc[] {

				docAccessor.newInitBannerDoc( "संपादकांची निवड",
						"Aug-17-mr-1.jpg",
						null,
						"/editorschoice",
						"PratilipiListApi",
						gson.fromJson( "{ \"language\":\"MARATHI\", \"state\":\"PUBLISHED\", \"listName\":\"editorschoice\" }", JsonElement.class ).getAsJsonObject() ),

				docAccessor.newInitBannerDoc( "वर्षाऋतूतील खास कथा",
						"Aug-17-mr-2.jpg",
						null,
						"/monsoon",
						"PratilipiListApi",
						gson.fromJson( "{ \"language\":\"MARATHI\", \"state\":\"PUBLISHED\", \"listName\":\"monsoon\" }", JsonElement.class ).getAsJsonObject() ),

				docAccessor.newInitBannerDoc( "३ एएम हाॅरर कलेक्शन",
						"Aug-17-mr-3.jpg",
						null,
						"/3am",
						"PratilipiListApi",
						gson.fromJson( "{ \"language\":\"MARATHI\", \"state\":\"PUBLISHED\", \"listName\":\"3am\" }", JsonElement.class ).getAsJsonObject() ),

				docAccessor.newInitBannerDoc( "प्रवास आणि सोबत - रोमांचक कथा",
						"Aug-17-mr-4.jpg",
						null,
						"/travel-companion",
						"PratilipiListApi",
						gson.fromJson( "{ \"language\":\"MARATHI\", \"state\":\"PUBLISHED\", \"listName\":\"travel-companion\" }", JsonElement.class ).getAsJsonObject() ),

				docAccessor.newInitBannerDoc( "भयकथा स्पर्धा",
						"Aug-17-mr-5.jpg",
						null,
						"/event/bhaykatha-spardha",
						"PratilipiListApi",
						gson.fromJson( "{ \"language\":\"MARATHI\", \"state\":\"PUBLISHED\", \"eventId\":6109272160075776 }", JsonElement.class ).getAsJsonObject() )

		};
			
		InitDoc mrInitDoc = docAccessor.newInitDoc();
		mrInitDoc.setBanners( Arrays.asList( initBannerDocs ) );
		langInitDocs.put( Language.MARATHI, mrInitDoc );
		
		
		initBannerDocs = new InitBannerDoc[] {

				docAccessor.newInitBannerDoc( "ഞങ്ങൾ തെരെഞ്ഞെടുത്തവ",
						"Aug-17-ml-1.jpg",
						null,
						"/editorschoice",
						"PratilipiListApi",
						gson.fromJson( "{ \"language\":\"MALAYALAM\", \"state\":\"PUBLISHED\", \"listName\":\"editorschoice\" }", JsonElement.class ).getAsJsonObject() ),

				docAccessor.newInitBannerDoc( "മണ്\u200Dസൂണ്\u200D സ്പെഷ്യല്\u200D കളക്ഷന്\u200D",
						"Aug-17-ml-2.jpg",
						null,
						"/monsoon",
						"PratilipiListApi",
						gson.fromJson( "{ \"language\":\"MALAYALAM\", \"state\":\"PUBLISHED\", \"listName\":\"monsoon\" }", JsonElement.class ).getAsJsonObject() ),

				docAccessor.newInitBannerDoc( "ഭീതിയുടെ കഥകള്\u200D",
						"Aug-17-ml-3.jpg",
						null,
						"/3am",
						"PratilipiListApi",
						gson.fromJson( "{ \"language\":\"MALAYALAM\", \"state\":\"PUBLISHED\", \"listName\":\"3am\" }", JsonElement.class ).getAsJsonObject() ),

				docAccessor.newInitBannerDoc( "യാത്രയില്\u200D വായിക്കാന്\u200D",
						"Aug-17-ml-4.jpg",
						null,
						"/travel-companion",
						"PratilipiListApi",
						gson.fromJson( "{ \"language\":\"MALAYALAM\", \"state\":\"PUBLISHED\", \"listName\":\"travel-companion\" }", JsonElement.class ).getAsJsonObject() ),

				docAccessor.newInitBannerDoc( "കവിതാ മഹോത്സവം",
						"Aug-17-ml-5.jpg",
						null,
						"/event/poetry-contest",
						"PratilipiListApi",
						gson.fromJson( "{ \"language\":\"MALAYALAM\", \"state\":\"PUBLISHED\", \"eventId\":6013749990260736 }", JsonElement.class ).getAsJsonObject() )

		};

		InitDoc mlInitDoc = docAccessor.newInitDoc();
		mlInitDoc.setBanners( Arrays.asList( initBannerDocs ) );
		langInitDocs.put( Language.MALAYALAM, mlInitDoc );
		
		
		initBannerDocs = new InitBannerDoc[] {

				docAccessor.newInitBannerDoc( "সম্পাদকীয় চয়ন",
						"Aug-17-bn-1.jpg",
						null,
						"/editorschoice",
						"PratilipiListApi",
						gson.fromJson( "{ \"language\":\"BENGALI\", \"state\":\"PUBLISHED\", \"listName\":\"editorschoice\" }", JsonElement.class ).getAsJsonObject() ),

				docAccessor.newInitBannerDoc( "বর্ষাঋতুর বিশেষ গল্প",
						"Aug-17-bn-2.jpg",
						null,
						"/monsoon",
						"PratilipiListApi",
						gson.fromJson( "{ \"language\":\"BENGALI\", \"state\":\"PUBLISHED\", \"listName\":\"monsoon\" }", JsonElement.class ).getAsJsonObject() ),

				docAccessor.newInitBannerDoc( "রাত তিনটের সময় রহস্য গল্প",
						"Aug-17-bn-3.jpg",
						null,
						"/3am",
						"PratilipiListApi",
						gson.fromJson( "{ \"language\":\"BENGALI\", \"state\":\"PUBLISHED\", \"listName\":\"3am\" }", JsonElement.class ).getAsJsonObject() ),

				docAccessor.newInitBannerDoc( "ভ্রমণ আর সাথে রোমাঞ্চকর কাহিনী",
						"Aug-17-bn-4.jpg",
						null,
						"/travel-companion",
						"PratilipiListApi",
						gson.fromJson( "{ \"language\":\"BENGALI\", \"state\":\"PUBLISHED\", \"listName\":\"travel-companion\" }", JsonElement.class ).getAsJsonObject() ),

				docAccessor.newInitBannerDoc( "প্রতিলিপি কবিতা মহোৎসব",
						"Aug-17-bn-5.jpg",
						null,
						"/event/pratilipi-kavita-mahotsav",
						"PratilipiListApi",
						gson.fromJson( "{ \"language\":\"BENGALI\", \"state\":\"PUBLISHED\", \"eventId\":5998859556749312 }", JsonElement.class ).getAsJsonObject() )

		};
			
		InitDoc bnInitDoc = docAccessor.newInitDoc();
		bnInitDoc.setBanners( Arrays.asList( initBannerDocs ) );
		langInitDocs.put( Language.BENGALI, bnInitDoc );
		
		
		initBannerDocs = new InitBannerDoc[] {

				docAccessor.newInitBannerDoc( "సంపాదకీయం",
						"Aug-17-te-1.jpg",
						null,
						"/editorschoice",
						"PratilipiListApi",
						gson.fromJson( "{ \"language\":\"TELUGU\", \"state\":\"PUBLISHED\", \"listName\":\"editorschoice\" }", JsonElement.class ).getAsJsonObject() ),

				docAccessor.newInitBannerDoc( "వర్షాకాలపు కథలు",
						"Aug-17-te-2.jpg",
						null,
						"/monsoon",
						"PratilipiListApi",
						gson.fromJson( "{ \"language\":\"TELUGU\", \"state\":\"PUBLISHED\", \"listName\":\"monsoon\" }", JsonElement.class ).getAsJsonObject() ),

				docAccessor.newInitBannerDoc( "3 A.M కలెక్షన్",
						"Aug-17-te-3.jpg",
						null,
						"/3am",
						"PratilipiListApi",
						gson.fromJson( "{ \"language\":\"TELUGU\", \"state\":\"PUBLISHED\", \"listName\":\"3am\" }", JsonElement.class ).getAsJsonObject() ),

				docAccessor.newInitBannerDoc( "ప్రయాణ సమయం కథలు",
						"Aug-17-te-4.jpg",
						null,
						"/travel-companion",
						"PratilipiListApi",
						gson.fromJson( "{ \"language\":\"TELUGU\", \"state\":\"PUBLISHED\", \"listName\":\"travel-companion\" }", JsonElement.class ).getAsJsonObject() ),

				docAccessor.newInitBannerDoc( "తెలుగు భాషా దినోత్సవం సందర్భంగా ప్రతిలిపి వారి హర్రర్ కథల పోటి",
						"Aug-17-te-5.jpg",
						null,
						"/event/horror-story-contest",
						"PratilipiListApi",
						gson.fromJson( "{ \"language\":\"TELUGU\", \"state\":\"PUBLISHED\", \"eventId\":5272200650162176 }", JsonElement.class ).getAsJsonObject() )

		};
			
		InitDoc teInitDoc = docAccessor.newInitDoc();
		teInitDoc.setBanners( Arrays.asList( initBannerDocs ) );
		langInitDocs.put( Language.TELUGU, teInitDoc );
		
		
		initBannerDocs = new InitBannerDoc[] {

				docAccessor.newInitBannerDoc( "ಸಂಪಾದಕರ ಆಯ್ಕೆ",
						"Aug-17-kn-1.jpg",
						null,
						"/editorschoice",
						"PratilipiListApi",
						gson.fromJson( "{ \"language\":\"KANNADA\", \"state\":\"PUBLISHED\", \"listName\":\"editorschoice\" }", JsonElement.class ).getAsJsonObject() ),

				docAccessor.newInitBannerDoc( "ಮುಂಗಾರಿನ ಸಂಗ್ರಹ",
						"Aug-17-kn-2.jpg",
						null,
						"/monsoon",
						"PratilipiListApi",
						gson.fromJson( "{ \"language\":\"KANNADA\", \"state\":\"PUBLISHED\", \"listName\":\"monsoon\" }", JsonElement.class ).getAsJsonObject() ),

				docAccessor.newInitBannerDoc( "ಘೋರರಾತ್ರಿಯ ಸಂಗ್ರಹ",
						"Aug-17-kn-3.jpg",
						null,
						"/3am",
						"PratilipiListApi",
						gson.fromJson( "{ \"language\":\"KANNADA\", \"state\":\"PUBLISHED\", \"listName\":\"3am\" }", JsonElement.class ).getAsJsonObject() ),

				docAccessor.newInitBannerDoc( "ಪ್ರಯಾಣ ಸಮಯದ ಕಥೆಗಳು",
						"Aug-17-kn-4.jpg",
						null,
						"/travel-companion",
						"PratilipiListApi",
						gson.fromJson( "{ \"language\":\"KANNADA\", \"state\":\"PUBLISHED\", \"listName\":\"travel-companion\" }", JsonElement.class ).getAsJsonObject() ),

				docAccessor.newInitBannerDoc( "ಕೈಯ್ಯ ಹಿಡಿದು, ಹೆಜ್ಜೆ ಬೆಸೆದು",
						"Aug-17-kn-5.jpg",
						null,
						"/event/kaiyya-hididu-hejje-besedu",
						"PratilipiListApi",
						gson.fromJson( "{ \"language\":\"KANNADA\", \"state\":\"PUBLISHED\", \"eventId\":4640217838387200 }", JsonElement.class ).getAsJsonObject() )

		};
			
		InitDoc knInitDoc = docAccessor.newInitDoc();
		knInitDoc.setBanners( Arrays.asList( initBannerDocs ) );
		langInitDocs.put( Language.KANNADA, knInitDoc );
		
		
		initBannerDocs = new InitBannerDoc[] {};
			
		InitDoc enInitDoc = docAccessor.newInitDoc();
		enInitDoc.setBanners( Arrays.asList( initBannerDocs ) );
		langInitDocs.put( Language.ENGLISH, enInitDoc );
		
	}
	
	
	public static boolean hasAccessToUpdateInit( Language language ) {

		return UserAccessUtil.hasUserAccess(
				AccessTokenFilter.getAccessToken().getUserId(),
				language,
				AccessType.INIT_UPDATE );
		
	}
	

	
	public static String createInitBannerUrl( Language language, String bannerId ) {
		return createInitBannerUrl( language, bannerId, null );
	}
	
	public static String createInitBannerUrl( Language language, String bannerId, Integer width ) {
		String url = "/init/banner?language=" + language + "&name=" + bannerId;
		if( width != null )
			url = url + "&width=" + width;
		if( SystemProperty.CDN != null )
			url = SystemProperty.CDN.replace( "*", "0" ) + url;
		return url;
	}


	public static BlobEntry getInitBanner( Language language, String name, Integer width )
			throws InvalidArgumentException, UnexpectedServerException {
		
		BlobEntry blobEntry = DataAccessorFactory.getBlobAccessor()
				.getBlob( "init/banners/" + language.getCode() + "/" + name );
		
		if( blobEntry == null ) {
			JsonObject errorMessages = new JsonObject();
			errorMessages.addProperty( "name", "Invalid banner name." );
			throw new InvalidArgumentException( errorMessages );
		}
			
		if( width != null )
			blobEntry.setData( ImageUtil.resize( blobEntry.getData(), width ) );
		
		return blobEntry;
		
	}

	public static List<InitBannerData> getInitBannerList( Language language ) {
		
		List<InitBannerDoc> initBanners = langInitDocs.get( language ).getBanners();
		List<InitBannerData> initBannerDataList = new ArrayList<>( initBanners.size() );

		for( InitBannerDoc initBanner : initBanners ) {
			InitBannerData initBannerData = new InitBannerData();
			initBannerData.setTitle( initBanner.getTitle() );
			initBannerData.setImageUrl( createInitBannerUrl( language, initBanner.getBanner() ) );
			initBannerData.setMiniImageUrl( createInitBannerUrl( language, initBanner.getBannerMini() ) );
			initBannerData.setActionUrl( initBanner.getActionUrl() );
			initBannerData.setApiName( initBanner.getApiName() );
			initBannerData.setApiRequest( initBanner.getApiRequest() );
			initBannerDataList.add( initBannerData );
		}
		
		return initBannerDataList;
	
	}
	
	public static String saveInitBanner( Language language, BlobEntry blobEntry )
			throws InsufficientAccessException, UnexpectedServerException {
		
		if( ! hasAccessToUpdateInit( language ) )
			throw new InsufficientAccessException();
		
		
		String name = new Date().getTime() + "";
		
		BlobAccessor blobAccessor = DataAccessorFactory.getBlobAccessor();
		blobEntry.setName( "init/banners/" + language.getCode() + "/" + name );
		blobAccessor.createOrUpdateBlob( blobEntry );
		
		return name;
		
	}
	
	
}

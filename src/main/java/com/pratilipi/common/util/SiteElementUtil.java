package com.pratilipi.common.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.pratilipi.common.type.Website;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.google.gson.JsonObject;
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.common.type.Language;
import com.pratilipi.common.type.PratilipiType;
import com.pratilipi.data.DataAccessor;
import com.pratilipi.i18n.I18n;

public class SiteElementUtil {

	public static final String defaultLang = "en";

	private static List<Map<String, Object>> getNavigationList( Language language ) {

		List<String> lines = null;
		try {
			String fileName = "curated/navigation." + language.getCode();
			InputStream inputStream = DataAccessor.class.getResource( fileName ).openStream();
			lines = IOUtils.readLines( inputStream, "UTF-8" );
			inputStream.close();
		} catch( NullPointerException | IOException e ) {
			System.out.println( "Failed to fetch " + language.getNameEn() + " navigation list." );
			lines = new ArrayList<>( 0 );
		}

		List<Map<String, Object>> navigationList = new ArrayList<>();

		String navigationListTitle = null;
		List<Map<String, String>> linkList = new ArrayList<>();

		for( String line : lines ) {
			line = line.trim();
			if( line.isEmpty() )
				continue;

			if( line.contains( "App#" ) ) {
				Map<String, String> navigationEntry = new HashMap<>();
				navigationEntry.put( "url", line.substring( 0, line.indexOf( ' ' ) ).trim() );
				line = line.substring( line.indexOf( ' ' ) ).trim();
				navigationEntry.put( "name", line.substring( 0, line.indexOf( "Analytics#" ) ).trim() );
				String analyticsInfo = line.substring( line.indexOf( "Analytics#categoryName::" ) + "Analytics#categoryName::".length(),
						line.indexOf( "App#" ) ).trim();
				navigationEntry.put( "analytics", analyticsInfo );
				String imageName = line.substring( line.indexOf( "App#imageName::" ) + "App#imageName::".length(), line.lastIndexOf( "App#" ) ).trim();
				navigationEntry.put( "imageName", imageName );
				linkList.add( navigationEntry );
			}
			else {
				if( navigationListTitle != null ) {
					Map<String, Object> navigation = new LinkedHashMap<>();
					navigation.put( "title", navigationListTitle );
					navigation.put( "linkList", linkList );
					navigationList.add( navigation );
					linkList = new ArrayList<>();
				}
				navigationListTitle = line;
			}
		}
		Map<String, Object> navigation = new LinkedHashMap<>();
		navigation.put( "title", navigationListTitle );
		navigation.put( "linkList", linkList );
		navigationList.add( navigation );

		return navigationList;

	}

	private static List<Map<String, Object>> getListTitleList( Language language )
			throws URISyntaxException, IOException {

		List<Map<String, Object>> listTilteList = new ArrayList<>();
		File directory = new File( DataAccessor.class.getResource( "curated/" ).toURI() );
		try {
			for( File aFile : directory.listFiles() ) {
				if( ! aFile.getName().startsWith( "list." + language.getCode() + "." ) )
					continue;
				String line = FileUtils.readLines( aFile, "UTF-8" ).get( 0 );
				String url = "/" + aFile.getName().substring( aFile.getName().lastIndexOf( "." ) + 1 );
				String title = null, titleEn = null;
				if( line.contains( "|" ) ) {
					title = line.substring( 0, line.indexOf( "|" ) );
					titleEn = line.substring( line.indexOf( "|" ) + 1 );
				} else {
					title = line;
				}
				Map<String, Object> listEntry = new HashMap<>();
				listEntry.put( "url", url );
				listEntry.put( "title", title );
				listEntry.put( "titleEn", titleEn );
				listTilteList.add( listEntry );
			}

		} catch( NullPointerException e ) {
			System.out.println( "Error reading list files: " + e );
		}

		return listTilteList;

	}

	public static List<String> getStaticUrlList( Language language )
			throws URISyntaxException, IOException {

		Set<String> staticUrlSet = new HashSet<>();
		File directory = new File( "src/main/java/com/pratilipi/site/page/data/" );
		try {
			for( File aFile : directory.listFiles() ) {
				if( aFile.getName().startsWith( "static." + language.getCode() + "." ) ||
						aFile.getName().startsWith( "static." + Language.ENGLISH.getCode() + "." ) ) {
					String url = "/" + aFile.getName().substring( aFile.getName().lastIndexOf( "." ) + 1 );
					staticUrlSet.add( url.replace( "_", "/" ) );
				}
			}

		} catch( NullPointerException e ) {
			System.out.println( "Error reading static files: " + e );
		}

		return new ArrayList<>( staticUrlSet );

	}

	private static List<Long> getAEEUserIdList() {
		Set<Long> aeeUserIdSet = new HashSet<>();
		for( Language language : Language.values() )
			aeeUserIdSet.addAll( UserAccessUtil.getAeeUserIdList( language ) );
		return new ArrayList<>( aeeUserIdSet );
	}
	
	private static List<JsonObject> getQuotes( Language language ) {

		List<JsonObject> quotes = new ArrayList<>();
		File file = new File( "src/main/java/com/pratilipi/site/pwa/quotes/quotes" + "." + language.getCode() );
		try {
			List<String> lines = FileUtils.readLines( file, "UTF-8" );
			for( String line : lines ) {
				if( line.trim().isEmpty() ) continue;
				JsonObject quote = new JsonObject();
				if( line.contains( "##" ) ) {
					quote.addProperty( "content", line.substring( 0, line.indexOf( "##" ) ).trim() );
					quote.addProperty( "name", line.substring( line.indexOf( "##" ) + 2 ).trim() );
				} else {
					quote.addProperty( "content", line );
				}
				quotes.add( quote );
			}
		} catch( NullPointerException | IOException e ) {
			System.out.println( "Error reading static files: " + e );
		}

		return quotes;

	}

	public static void main( String args[] ) throws IOException,
			UnexpectedServerException, URISyntaxException,
			ClassNotFoundException {

		String elementFolderName = args[0];
		String i18nElementFolderNamePrefix = args[1];
		String framework = args[2];

		if( framework.equals( "polymer" ) ) {
			createPolymerI18nFiles( elementFolderName, i18nElementFolderNamePrefix );
		} else if( framework.equals( "knockout" ) ) {
			createPWAI18nFiles( elementFolderName, i18nElementFolderNamePrefix );
		}
	}

	public static void createPolymerI18nFiles( String elementFolderName, String i18nElementFolderNamePrefix )
			throws IOException, UnexpectedServerException {

		for( Language language : Language.values() ) {
			File elementFolder = new File( elementFolderName );
			for( String elementName : elementFolder.list() ) {
				if( !elementName.endsWith(".html") )
					continue;
				File element = new File( elementFolder, elementName );
				if( element.isDirectory() )
					continue;

				// Data model required for i18n element generation
				Map<String, Object> dataModel = new HashMap<>();
				dataModel.put("lang", language.getCode());
				dataModel.put("language", language );
				dataModel.put("domain", language.getHostName());
				dataModel.put("fbAppId", "293990794105516");
				dataModel.put("googleClientId", "659873510744-kfim969enh181h4gbctffrjg5j47tfuq.apps.googleusercontent.com");
				dataModel.put("_strings", I18n.getStrings(language));

				// I18n element file output stream
				File i18nElementFolder = new File(i18nElementFolderNamePrefix + language.getCode());
				i18nElementFolder.mkdir();
				File i18nElement = new File(i18nElementFolder, elementName);
				i18nElement.createNewFile();

				OutputStreamWriter i18nElementOS = new OutputStreamWriter(new FileOutputStream(i18nElement), "UTF-8");

				// The magic
				FreeMarkerUtil.processTemplate(
						dataModel,
						elementFolderName + "/" + elementName,
						i18nElementOS);

				// closing the output stream
				i18nElementOS.close();
			}
		}
	}

	public static void createPWAI18nFiles( String elementFolderName, String i18nElementFolderNamePrefix )
			throws URISyntaxException, IOException, UnexpectedServerException {

		List<String> PWAElementList = new ArrayList<>();
		for( String elementName : new File( "src/main/webapp/pwa-elements" ).list() )
			PWAElementList.add( elementName );

		List<Long> aEEUserIdList = getAEEUserIdList();

		for( Website website : Website.values() ) {

			Language language = website.getDisplayLanguage();

			File elementFolder = new File( elementFolderName );
			for( String elementName : elementFolder.list() ) {

				if( ! elementName.endsWith( ".html" ) &&
						! elementName.endsWith( ".js" ) &&
						! elementName.endsWith( ".json" ) )
					continue;

				File element = new File( elementFolder, elementName );
				if( element.isDirectory() )
					continue;

				// Data model required for i18n element generation
				Map<String, Object> dataModel = new HashMap<>();
				dataModel.put( "PWAElementList", PWAElementList );
				dataModel.put( "website", website );
				dataModel.put( "lang", language.getCode() );
				dataModel.put( "language", language );
				dataModel.put( "domain", website.getHostName() );
				dataModel.put( "fbAppId", "293990794105516" );
				dataModel.put( "googleClientId", "659873510744-kfim969enh181h4gbctffrjg5j47tfuq.apps.googleusercontent.com" );
				dataModel.put( "firebaseLibrary", "https://www.gstatic.com/firebasejs/3.6.10/firebase.js" );
				dataModel.put( "firebaseApiKey", _isDevoFirebaseEnv( website ) ? "AIzaSyAdtChdjMgN5U1qJobXvgJ_rIHBA_bqIro" : "AIzaSyAAnK0-vDmY1UEcrRRbCzXgdpF2oQn-E0w" );
				dataModel.put( "firebaseAuthDomain", _isDevoFirebaseEnv( website ) ? "devo-pratilipi.firebaseapp.com" : "prod-pratilipi.firebaseapp.com" );
				dataModel.put( "firebaseDatabaseUrl", _isDevoFirebaseEnv( website ) ? "https://devo-pratilipi.firebaseio.com" : "https://prod-pratilipi.firebaseio.com" );
				dataModel.put( "firebaseProjectId", _isDevoFirebaseEnv( website ) ? "devo-pratilipi" : "prod-pratilipi" );
				dataModel.put( "firebaseStorageBucket", _isDevoFirebaseEnv( website ) ? "devo-pratilipi.appspot.com" : "prod-pratilipi.appspot.com" );
				dataModel.put( "_strings", I18n.getStrings( language ) );
				dataModel.put( "ga_website", "PWA" );
				dataModel.put( "ga_websiteMode", "Standard" );
				dataModel.put( "ga_websiteVersion", "Mark-7" );
				dataModel.put( "date", new Date() );

				List<Map<String, Object>> pratilipiTypes = new ArrayList<>();
				for( PratilipiType pratilipiType : PratilipiType.values() ) {
					Map<String, Object> type = new HashMap<>();
					type.put( "name", I18n.getString( pratilipiType.getStringId(), language ) );
					type.put( "namePlural", I18n.getString( pratilipiType.getPluralStringId(), language ) );
					type.put( "value", pratilipiType.name() );
					pratilipiTypes.add( type );
				}
				dataModel.put( "pratilipiTypes", pratilipiTypes );

				// Language list - Exclude English ( for options in header / author profile / select language in frontend )
				List<Map<String, Object>> languageList = new ArrayList<>();
				for( Language lang : Language.values() ) {
					if( lang == Language.ENGLISH ) continue;
					Map<String, Object> langMap = new HashMap<>();
					langMap.put( "value", lang );
					langMap.put( "code", lang.getCode() );
					langMap.put( "hostName", "https://" + lang.getHostName() );
					langMap.put( "name", lang.getName() );
					langMap.put( "nameEn", lang.getNameEn() );
					languageList.add(langMap);
				}
				dataModel.put( "languageList", languageList );

				dataModel.put( "navigationList", getNavigationList( website.getDisplayLanguage() ) );
				dataModel.put( "listTitleList", getListTitleList( website.getDisplayLanguage() ) );
				dataModel.put( "staticUrlList", getStaticUrlList( website.getDisplayLanguage() ) );
				dataModel.put( "aEEUserIdList", aEEUserIdList );
				dataModel.put( "quoteList", getQuotes( website.getDisplayLanguage() ) );

				// I18n element file output stream
				File i18nElement = null;
				String elName = null;
				if( elementName.endsWith( ".html" ) ) {
					elName = elementName.substring( 0, elementName.indexOf( ".html" ) ) + "-" + website.name() + ".html";
				} else if( elementName.endsWith( ".js" ) ) {
					elName = elementName.substring( 0, elementName.indexOf( ".js" ) ) + "-" + website.name() + ".js";
				} else if( elementName.endsWith( ".json" ) ) {
					elName = elementName.substring( 0, elementName.indexOf( ".json" ) ) + "-" + website.name() + ".json";
				}

				i18nElement = new File( i18nElementFolderNamePrefix, elName );
				i18nElement.createNewFile();


				OutputStreamWriter i18nElementOS = new OutputStreamWriter( new FileOutputStream( i18nElement ), "UTF-8" );

				// The magic
				FreeMarkerUtil.processTemplate(
						dataModel,
						elementFolderName + "/" + elementName,
						i18nElementOS );

				// closing the output stream
				i18nElementOS.close();

			}

		}
	}

	private static boolean _isDevoFirebaseEnv( Website website ) {
		if( website == Website.ALPHA )
			return true;
		else if( website.name().startsWith( "DEVO_" ) )
			return true;
		else
			return false;
	}

}

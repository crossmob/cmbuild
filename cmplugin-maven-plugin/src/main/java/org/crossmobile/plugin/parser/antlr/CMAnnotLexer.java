// Generated from CMAnnot.g4 by ANTLR 4.7.1
package org.crossmobile.plugin.parser.antlr;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class CMAnnotLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.7.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, T__20=21, T__21=22, T__22=23, T__23=24, 
		T__24=25, T__25=26, T__26=27, T__27=28, T__28=29, T__29=30, T__30=31, 
		T__31=32, T__32=33, T__33=34, T__34=35, T__35=36, T__36=37, LINE_COMMENT=38, 
		COMMENT=39, WS=40, UI_APPEARANCE_SELECTOR=41, UIKIT_EXTERN=42, NULLABLE=43, 
		NONNULL=44, EXTERN=45, ONEWAY=46, UNSIGNED=47, KINDOF=48, ID=49, VERSION=50, 
		NUM=51;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
		"T__9", "T__10", "T__11", "T__12", "T__13", "T__14", "T__15", "T__16", 
		"T__17", "T__18", "T__19", "T__20", "T__21", "T__22", "T__23", "T__24", 
		"T__25", "T__26", "T__27", "T__28", "T__29", "T__30", "T__31", "T__32", 
		"T__33", "T__34", "T__35", "T__36", "LINE_COMMENT", "COMMENT", "WS", "UI_APPEARANCE_SELECTOR", 
		"UIKIT_EXTERN", "NULLABLE", "NONNULL", "EXTERN", "ONEWAY", "UNSIGNED", 
		"KINDOF", "ID", "VERSION", "NUM", "HEX", "INT"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'@interface'", "'@protocol'", "'('", "')'", "'@end'", "'<'", "','", 
		"'>'", "':'", "'@property'", "';'", "'atomic'", "'nonatomic'", "'readwrite'", 
		"'nullable'", "'class'", "'retain'", "'strong'", "'assign'", "'weak'", 
		"'copy'", "'readonly'", "'getter'", "'='", "'const'", "'...'", "'+'", 
		"'-'", "'['", "']'", "'*'", "'id<'", "'^'", "'@private'", "'@public'", 
		"'@protected'", "'@package'", null, null, null, "'UI_APPEARANCE_SELECTOR'", 
		"'UIKIT_EXTERN'", "'_Nullable'", "'_Nonnull'", "'extern'", "'oneway'", 
		"'unsigned'", "'__kindof'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, "LINE_COMMENT", "COMMENT", "WS", "UI_APPEARANCE_SELECTOR", 
		"UIKIT_EXTERN", "NULLABLE", "NONNULL", "EXTERN", "ONEWAY", "UNSIGNED", 
		"KINDOF", "ID", "VERSION", "NUM"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public CMAnnotLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "CMAnnot.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\65\u01de\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t"+
		" \4!\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t"+
		"+\4,\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64"+
		"\t\64\4\65\t\65\4\66\t\66\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\6\3"+
		"\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\13\3\13\3\13\3\13\3"+
		"\13\3\13\3\13\3\13\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\16\3\16\3\16"+
		"\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\17\3\17\3\17\3\17\3\17\3\17\3\17"+
		"\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\21\3\21"+
		"\3\21\3\21\3\21\3\21\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\23\3\23\3\23"+
		"\3\23\3\23\3\23\3\23\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\25\3\25\3\25"+
		"\3\25\3\25\3\26\3\26\3\26\3\26\3\26\3\27\3\27\3\27\3\27\3\27\3\27\3\27"+
		"\3\27\3\27\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\31\3\31\3\32\3\32\3\32"+
		"\3\32\3\32\3\32\3\33\3\33\3\33\3\33\3\34\3\34\3\35\3\35\3\36\3\36\3\37"+
		"\3\37\3 \3 \3!\3!\3!\3!\3\"\3\"\3#\3#\3#\3#\3#\3#\3#\3#\3#\3$\3$\3$\3"+
		"$\3$\3$\3$\3$\3%\3%\3%\3%\3%\3%\3%\3%\3%\3%\3%\3&\3&\3&\3&\3&\3&\3&\3"+
		"&\3&\3\'\3\'\3\'\3\'\7\'\u013e\n\'\f\'\16\'\u0141\13\'\3\'\5\'\u0144\n"+
		"\'\3\'\3\'\3\'\3\'\3(\3(\3(\3(\7(\u014e\n(\f(\16(\u0151\13(\3(\3(\3(\3"+
		"(\3(\3)\3)\3)\3)\3*\3*\3*\3*\3*\3*\3*\3*\3*\3*\3*\3*\3*\3*\3*\3*\3*\3"+
		"*\3*\3*\3*\3*\3*\3*\3*\3+\3+\3+\3+\3+\3+\3+\3+\3+\3+\3+\3+\3+\3+\3+\3"+
		",\3,\3,\3,\3,\3,\3,\3,\3,\3,\3,\3,\3-\3-\3-\3-\3-\3-\3-\3-\3-\3-\3-\3"+
		".\3.\3.\3.\3.\3.\3.\3.\3.\3/\3/\3/\3/\3/\3/\3/\3/\3/\3\60\3\60\3\60\3"+
		"\60\3\60\3\60\3\60\3\60\3\60\3\60\3\60\3\61\3\61\3\61\3\61\3\61\3\61\3"+
		"\61\3\61\3\61\3\61\3\61\3\62\3\62\7\62\u01c5\n\62\f\62\16\62\u01c8\13"+
		"\62\3\63\3\63\3\63\3\63\3\64\3\64\5\64\u01d0\n\64\3\65\3\65\3\65\3\65"+
		"\3\65\3\66\5\66\u01d8\n\66\3\66\6\66\u01db\n\66\r\66\16\66\u01dc\4\u013f"+
		"\u014f\2\67\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33"+
		"\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33\65\34\67"+
		"\359\36;\37= ?!A\"C#E$G%I&K\'M(O)Q*S+U,W-Y.[/]\60_\61a\62c\63e\64g\65"+
		"i\2k\2\3\2\6\5\2\13\f\17\17\"\"\5\2C\\aac|\6\2\62;C\\aac|\3\2\62;\2\u01e2"+
		"\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2"+
		"\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2"+
		"\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2"+
		"\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2"+
		"\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3"+
		"\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2"+
		"\2\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\2O\3\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2"+
		"U\3\2\2\2\2W\3\2\2\2\2Y\3\2\2\2\2[\3\2\2\2\2]\3\2\2\2\2_\3\2\2\2\2a\3"+
		"\2\2\2\2c\3\2\2\2\2e\3\2\2\2\2g\3\2\2\2\3m\3\2\2\2\5x\3\2\2\2\7\u0082"+
		"\3\2\2\2\t\u0084\3\2\2\2\13\u0086\3\2\2\2\r\u008b\3\2\2\2\17\u008d\3\2"+
		"\2\2\21\u008f\3\2\2\2\23\u0091\3\2\2\2\25\u0093\3\2\2\2\27\u009d\3\2\2"+
		"\2\31\u009f\3\2\2\2\33\u00a6\3\2\2\2\35\u00b0\3\2\2\2\37\u00ba\3\2\2\2"+
		"!\u00c3\3\2\2\2#\u00c9\3\2\2\2%\u00d0\3\2\2\2\'\u00d7\3\2\2\2)\u00de\3"+
		"\2\2\2+\u00e3\3\2\2\2-\u00e8\3\2\2\2/\u00f1\3\2\2\2\61\u00f8\3\2\2\2\63"+
		"\u00fa\3\2\2\2\65\u0100\3\2\2\2\67\u0104\3\2\2\29\u0106\3\2\2\2;\u0108"+
		"\3\2\2\2=\u010a\3\2\2\2?\u010c\3\2\2\2A\u010e\3\2\2\2C\u0112\3\2\2\2E"+
		"\u0114\3\2\2\2G\u011d\3\2\2\2I\u0125\3\2\2\2K\u0130\3\2\2\2M\u0139\3\2"+
		"\2\2O\u0149\3\2\2\2Q\u0157\3\2\2\2S\u015b\3\2\2\2U\u0174\3\2\2\2W\u0183"+
		"\3\2\2\2Y\u018f\3\2\2\2[\u019a\3\2\2\2]\u01a3\3\2\2\2_\u01ac\3\2\2\2a"+
		"\u01b7\3\2\2\2c\u01c2\3\2\2\2e\u01c9\3\2\2\2g\u01cf\3\2\2\2i\u01d1\3\2"+
		"\2\2k\u01d7\3\2\2\2mn\7B\2\2no\7k\2\2op\7p\2\2pq\7v\2\2qr\7g\2\2rs\7t"+
		"\2\2st\7h\2\2tu\7c\2\2uv\7e\2\2vw\7g\2\2w\4\3\2\2\2xy\7B\2\2yz\7r\2\2"+
		"z{\7t\2\2{|\7q\2\2|}\7v\2\2}~\7q\2\2~\177\7e\2\2\177\u0080\7q\2\2\u0080"+
		"\u0081\7n\2\2\u0081\6\3\2\2\2\u0082\u0083\7*\2\2\u0083\b\3\2\2\2\u0084"+
		"\u0085\7+\2\2\u0085\n\3\2\2\2\u0086\u0087\7B\2\2\u0087\u0088\7g\2\2\u0088"+
		"\u0089\7p\2\2\u0089\u008a\7f\2\2\u008a\f\3\2\2\2\u008b\u008c\7>\2\2\u008c"+
		"\16\3\2\2\2\u008d\u008e\7.\2\2\u008e\20\3\2\2\2\u008f\u0090\7@\2\2\u0090"+
		"\22\3\2\2\2\u0091\u0092\7<\2\2\u0092\24\3\2\2\2\u0093\u0094\7B\2\2\u0094"+
		"\u0095\7r\2\2\u0095\u0096\7t\2\2\u0096\u0097\7q\2\2\u0097\u0098\7r\2\2"+
		"\u0098\u0099\7g\2\2\u0099\u009a\7t\2\2\u009a\u009b\7v\2\2\u009b\u009c"+
		"\7{\2\2\u009c\26\3\2\2\2\u009d\u009e\7=\2\2\u009e\30\3\2\2\2\u009f\u00a0"+
		"\7c\2\2\u00a0\u00a1\7v\2\2\u00a1\u00a2\7q\2\2\u00a2\u00a3\7o\2\2\u00a3"+
		"\u00a4\7k\2\2\u00a4\u00a5\7e\2\2\u00a5\32\3\2\2\2\u00a6\u00a7\7p\2\2\u00a7"+
		"\u00a8\7q\2\2\u00a8\u00a9\7p\2\2\u00a9\u00aa\7c\2\2\u00aa\u00ab\7v\2\2"+
		"\u00ab\u00ac\7q\2\2\u00ac\u00ad\7o\2\2\u00ad\u00ae\7k\2\2\u00ae\u00af"+
		"\7e\2\2\u00af\34\3\2\2\2\u00b0\u00b1\7t\2\2\u00b1\u00b2\7g\2\2\u00b2\u00b3"+
		"\7c\2\2\u00b3\u00b4\7f\2\2\u00b4\u00b5\7y\2\2\u00b5\u00b6\7t\2\2\u00b6"+
		"\u00b7\7k\2\2\u00b7\u00b8\7v\2\2\u00b8\u00b9\7g\2\2\u00b9\36\3\2\2\2\u00ba"+
		"\u00bb\7p\2\2\u00bb\u00bc\7w\2\2\u00bc\u00bd\7n\2\2\u00bd\u00be\7n\2\2"+
		"\u00be\u00bf\7c\2\2\u00bf\u00c0\7d\2\2\u00c0\u00c1\7n\2\2\u00c1\u00c2"+
		"\7g\2\2\u00c2 \3\2\2\2\u00c3\u00c4\7e\2\2\u00c4\u00c5\7n\2\2\u00c5\u00c6"+
		"\7c\2\2\u00c6\u00c7\7u\2\2\u00c7\u00c8\7u\2\2\u00c8\"\3\2\2\2\u00c9\u00ca"+
		"\7t\2\2\u00ca\u00cb\7g\2\2\u00cb\u00cc\7v\2\2\u00cc\u00cd\7c\2\2\u00cd"+
		"\u00ce\7k\2\2\u00ce\u00cf\7p\2\2\u00cf$\3\2\2\2\u00d0\u00d1\7u\2\2\u00d1"+
		"\u00d2\7v\2\2\u00d2\u00d3\7t\2\2\u00d3\u00d4\7q\2\2\u00d4\u00d5\7p\2\2"+
		"\u00d5\u00d6\7i\2\2\u00d6&\3\2\2\2\u00d7\u00d8\7c\2\2\u00d8\u00d9\7u\2"+
		"\2\u00d9\u00da\7u\2\2\u00da\u00db\7k\2\2\u00db\u00dc\7i\2\2\u00dc\u00dd"+
		"\7p\2\2\u00dd(\3\2\2\2\u00de\u00df\7y\2\2\u00df\u00e0\7g\2\2\u00e0\u00e1"+
		"\7c\2\2\u00e1\u00e2\7m\2\2\u00e2*\3\2\2\2\u00e3\u00e4\7e\2\2\u00e4\u00e5"+
		"\7q\2\2\u00e5\u00e6\7r\2\2\u00e6\u00e7\7{\2\2\u00e7,\3\2\2\2\u00e8\u00e9"+
		"\7t\2\2\u00e9\u00ea\7g\2\2\u00ea\u00eb\7c\2\2\u00eb\u00ec\7f\2\2\u00ec"+
		"\u00ed\7q\2\2\u00ed\u00ee\7p\2\2\u00ee\u00ef\7n\2\2\u00ef\u00f0\7{\2\2"+
		"\u00f0.\3\2\2\2\u00f1\u00f2\7i\2\2\u00f2\u00f3\7g\2\2\u00f3\u00f4\7v\2"+
		"\2\u00f4\u00f5\7v\2\2\u00f5\u00f6\7g\2\2\u00f6\u00f7\7t\2\2\u00f7\60\3"+
		"\2\2\2\u00f8\u00f9\7?\2\2\u00f9\62\3\2\2\2\u00fa\u00fb\7e\2\2\u00fb\u00fc"+
		"\7q\2\2\u00fc\u00fd\7p\2\2\u00fd\u00fe\7u\2\2\u00fe\u00ff\7v\2\2\u00ff"+
		"\64\3\2\2\2\u0100\u0101\7\60\2\2\u0101\u0102\7\60\2\2\u0102\u0103\7\60"+
		"\2\2\u0103\66\3\2\2\2\u0104\u0105\7-\2\2\u01058\3\2\2\2\u0106\u0107\7"+
		"/\2\2\u0107:\3\2\2\2\u0108\u0109\7]\2\2\u0109<\3\2\2\2\u010a\u010b\7_"+
		"\2\2\u010b>\3\2\2\2\u010c\u010d\7,\2\2\u010d@\3\2\2\2\u010e\u010f\7k\2"+
		"\2\u010f\u0110\7f\2\2\u0110\u0111\7>\2\2\u0111B\3\2\2\2\u0112\u0113\7"+
		"`\2\2\u0113D\3\2\2\2\u0114\u0115\7B\2\2\u0115\u0116\7r\2\2\u0116\u0117"+
		"\7t\2\2\u0117\u0118\7k\2\2\u0118\u0119\7x\2\2\u0119\u011a\7c\2\2\u011a"+
		"\u011b\7v\2\2\u011b\u011c\7g\2\2\u011cF\3\2\2\2\u011d\u011e\7B\2\2\u011e"+
		"\u011f\7r\2\2\u011f\u0120\7w\2\2\u0120\u0121\7d\2\2\u0121\u0122\7n\2\2"+
		"\u0122\u0123\7k\2\2\u0123\u0124\7e\2\2\u0124H\3\2\2\2\u0125\u0126\7B\2"+
		"\2\u0126\u0127\7r\2\2\u0127\u0128\7t\2\2\u0128\u0129\7q\2\2\u0129\u012a"+
		"\7v\2\2\u012a\u012b\7g\2\2\u012b\u012c\7e\2\2\u012c\u012d\7v\2\2\u012d"+
		"\u012e\7g\2\2\u012e\u012f\7f\2\2\u012fJ\3\2\2\2\u0130\u0131\7B\2\2\u0131"+
		"\u0132\7r\2\2\u0132\u0133\7c\2\2\u0133\u0134\7e\2\2\u0134\u0135\7m\2\2"+
		"\u0135\u0136\7c\2\2\u0136\u0137\7i\2\2\u0137\u0138\7g\2\2\u0138L\3\2\2"+
		"\2\u0139\u013a\7\61\2\2\u013a\u013b\7\61\2\2\u013b\u013f\3\2\2\2\u013c"+
		"\u013e\13\2\2\2\u013d\u013c\3\2\2\2\u013e\u0141\3\2\2\2\u013f\u0140\3"+
		"\2\2\2\u013f\u013d\3\2\2\2\u0140\u0143\3\2\2\2\u0141\u013f\3\2\2\2\u0142"+
		"\u0144\7\17\2\2\u0143\u0142\3\2\2\2\u0143\u0144\3\2\2\2\u0144\u0145\3"+
		"\2\2\2\u0145\u0146\7\f\2\2\u0146\u0147\3\2\2\2\u0147\u0148\b\'\2\2\u0148"+
		"N\3\2\2\2\u0149\u014a\7\61\2\2\u014a\u014b\7,\2\2\u014b\u014f\3\2\2\2"+
		"\u014c\u014e\13\2\2\2\u014d\u014c\3\2\2\2\u014e\u0151\3\2\2\2\u014f\u0150"+
		"\3\2\2\2\u014f\u014d\3\2\2\2\u0150\u0152\3\2\2\2\u0151\u014f\3\2\2\2\u0152"+
		"\u0153\7,\2\2\u0153\u0154\7\61\2\2\u0154\u0155\3\2\2\2\u0155\u0156\b("+
		"\2\2\u0156P\3\2\2\2\u0157\u0158\t\2\2\2\u0158\u0159\3\2\2\2\u0159\u015a"+
		"\b)\2\2\u015aR\3\2\2\2\u015b\u015c\7W\2\2\u015c\u015d\7K\2\2\u015d\u015e"+
		"\7a\2\2\u015e\u015f\7C\2\2\u015f\u0160\7R\2\2\u0160\u0161\7R\2\2\u0161"+
		"\u0162\7G\2\2\u0162\u0163\7C\2\2\u0163\u0164\7T\2\2\u0164\u0165\7C\2\2"+
		"\u0165\u0166\7P\2\2\u0166\u0167\7E\2\2\u0167\u0168\7G\2\2\u0168\u0169"+
		"\7a\2\2\u0169\u016a\7U\2\2\u016a\u016b\7G\2\2\u016b\u016c\7N\2\2\u016c"+
		"\u016d\7G\2\2\u016d\u016e\7E\2\2\u016e\u016f\7V\2\2\u016f\u0170\7Q\2\2"+
		"\u0170\u0171\7T\2\2\u0171\u0172\3\2\2\2\u0172\u0173\b*\2\2\u0173T\3\2"+
		"\2\2\u0174\u0175\7W\2\2\u0175\u0176\7K\2\2\u0176\u0177\7M\2\2\u0177\u0178"+
		"\7K\2\2\u0178\u0179\7V\2\2\u0179\u017a\7a\2\2\u017a\u017b\7G\2\2\u017b"+
		"\u017c\7Z\2\2\u017c\u017d\7V\2\2\u017d\u017e\7G\2\2\u017e\u017f\7T\2\2"+
		"\u017f\u0180\7P\2\2\u0180\u0181\3\2\2\2\u0181\u0182\b+\2\2\u0182V\3\2"+
		"\2\2\u0183\u0184\7a\2\2\u0184\u0185\7P\2\2\u0185\u0186\7w\2\2\u0186\u0187"+
		"\7n\2\2\u0187\u0188\7n\2\2\u0188\u0189\7c\2\2\u0189\u018a\7d\2\2\u018a"+
		"\u018b\7n\2\2\u018b\u018c\7g\2\2\u018c\u018d\3\2\2\2\u018d\u018e\b,\2"+
		"\2\u018eX\3\2\2\2\u018f\u0190\7a\2\2\u0190\u0191\7P\2\2\u0191\u0192\7"+
		"q\2\2\u0192\u0193\7p\2\2\u0193\u0194\7p\2\2\u0194\u0195\7w\2\2\u0195\u0196"+
		"\7n\2\2\u0196\u0197\7n\2\2\u0197\u0198\3\2\2\2\u0198\u0199\b-\2\2\u0199"+
		"Z\3\2\2\2\u019a\u019b\7g\2\2\u019b\u019c\7z\2\2\u019c\u019d\7v\2\2\u019d"+
		"\u019e\7g\2\2\u019e\u019f\7t\2\2\u019f\u01a0\7p\2\2\u01a0\u01a1\3\2\2"+
		"\2\u01a1\u01a2\b.\2\2\u01a2\\\3\2\2\2\u01a3\u01a4\7q\2\2\u01a4\u01a5\7"+
		"p\2\2\u01a5\u01a6\7g\2\2\u01a6\u01a7\7y\2\2\u01a7\u01a8\7c\2\2\u01a8\u01a9"+
		"\7{\2\2\u01a9\u01aa\3\2\2\2\u01aa\u01ab\b/\2\2\u01ab^\3\2\2\2\u01ac\u01ad"+
		"\7w\2\2\u01ad\u01ae\7p\2\2\u01ae\u01af\7u\2\2\u01af\u01b0\7k\2\2\u01b0"+
		"\u01b1\7i\2\2\u01b1\u01b2\7p\2\2\u01b2\u01b3\7g\2\2\u01b3\u01b4\7f\2\2"+
		"\u01b4\u01b5\3\2\2\2\u01b5\u01b6\b\60\2\2\u01b6`\3\2\2\2\u01b7\u01b8\7"+
		"a\2\2\u01b8\u01b9\7a\2\2\u01b9\u01ba\7m\2\2\u01ba\u01bb\7k\2\2\u01bb\u01bc"+
		"\7p\2\2\u01bc\u01bd\7f\2\2\u01bd\u01be\7q\2\2\u01be\u01bf\7h\2\2\u01bf"+
		"\u01c0\3\2\2\2\u01c0\u01c1\b\61\2\2\u01c1b\3\2\2\2\u01c2\u01c6\t\3\2\2"+
		"\u01c3\u01c5\t\4\2\2\u01c4\u01c3\3\2\2\2\u01c5\u01c8\3\2\2\2\u01c6\u01c4"+
		"\3\2\2\2\u01c6\u01c7\3\2\2\2\u01c7d\3\2\2\2\u01c8\u01c6\3\2\2\2\u01c9"+
		"\u01ca\t\5\2\2\u01ca\u01cb\7a\2\2\u01cb\u01cc\t\5\2\2\u01ccf\3\2\2\2\u01cd"+
		"\u01d0\5k\66\2\u01ce\u01d0\5i\65\2\u01cf\u01cd\3\2\2\2\u01cf\u01ce\3\2"+
		"\2\2\u01d0h\3\2\2\2\u01d1\u01d2\7\62\2\2\u01d2\u01d3\7z\2\2\u01d3\u01d4"+
		"\3\2\2\2\u01d4\u01d5\5k\66\2\u01d5j\3\2\2\2\u01d6\u01d8\7/\2\2\u01d7\u01d6"+
		"\3\2\2\2\u01d7\u01d8\3\2\2\2\u01d8\u01da\3\2\2\2\u01d9\u01db\t\5\2\2\u01da"+
		"\u01d9\3\2\2\2\u01db\u01dc\3\2\2\2\u01dc\u01da\3\2\2\2\u01dc\u01dd\3\2"+
		"\2\2\u01ddl\3\2\2\2\n\2\u013f\u0143\u014f\u01c6\u01cf\u01d7\u01dc\3\b"+
		"\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}
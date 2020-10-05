// Generated from CMAnnot.g4 by ANTLR 4.7.1
package org.crossmobile.plugin.parser.antlr;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class CMAnnotParser extends Parser {
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
	public static final int
		RULE_classdef = 0, RULE_protinheritance = 1, RULE_inheritance = 2, RULE_method = 3, 
		RULE_property = 4, RULE_propertydef = 5, RULE_cparts = 6, RULE_func = 7, 
		RULE_extconst = 8, RULE_selector = 9, RULE_selectorNamedParam = 10, RULE_selectorParam = 11, 
		RULE_returntype = 12, RULE_listofvariables = 13, RULE_var_type_name = 14, 
		RULE_variable = 15, RULE_vartype = 16, RULE_block = 17, RULE_simple_vartype_name = 18, 
		RULE_simple_vartype = 19, RULE_visibility = 20;
	public static final String[] ruleNames = {
		"classdef", "protinheritance", "inheritance", "method", "property", "propertydef", 
		"cparts", "func", "extconst", "selector", "selectorNamedParam", "selectorParam", 
		"returntype", "listofvariables", "var_type_name", "variable", "vartype", 
		"block", "simple_vartype_name", "simple_vartype", "visibility"
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

	@Override
	public String getGrammarFileName() { return "CMAnnot.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public CMAnnotParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class ClassdefContext extends ParserRuleContext {
		public Token classtype;
		public Token classname;
		public Token category;
		public List<TerminalNode> ID() { return getTokens(CMAnnotParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(CMAnnotParser.ID, i);
		}
		public InheritanceContext inheritance() {
			return getRuleContext(InheritanceContext.class,0);
		}
		public List<MethodContext> method() {
			return getRuleContexts(MethodContext.class);
		}
		public MethodContext method(int i) {
			return getRuleContext(MethodContext.class,i);
		}
		public ClassdefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classdef; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CMAnnotListener ) ((CMAnnotListener)listener).enterClassdef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CMAnnotListener ) ((CMAnnotListener)listener).exitClassdef(this);
		}
	}

	public final ClassdefContext classdef() throws RecognitionException {
		ClassdefContext _localctx = new ClassdefContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_classdef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(42);
			((ClassdefContext)_localctx).classtype = _input.LT(1);
			_la = _input.LA(1);
			if ( !(_la==T__0 || _la==T__1) ) {
				((ClassdefContext)_localctx).classtype = (Token)_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(43);
			((ClassdefContext)_localctx).classname = match(ID);
			setState(47);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__2) {
				{
				setState(44);
				match(T__2);
				setState(45);
				((ClassdefContext)_localctx).category = match(ID);
				setState(46);
				match(T__3);
				}
			}

			setState(50);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__8) {
				{
				setState(49);
				inheritance();
				}
			}

			setState(55);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__9) | (1L << T__24) | (1L << T__26) | (1L << T__27) | (1L << T__31) | (1L << ID))) != 0)) {
				{
				{
				setState(52);
				method();
				}
				}
				setState(57);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(58);
			match(T__4);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ProtinheritanceContext extends ParserRuleContext {
		public Token inh;
		public List<TerminalNode> ID() { return getTokens(CMAnnotParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(CMAnnotParser.ID, i);
		}
		public ProtinheritanceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_protinheritance; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CMAnnotListener ) ((CMAnnotListener)listener).enterProtinheritance(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CMAnnotListener ) ((CMAnnotListener)listener).exitProtinheritance(this);
		}
	}

	public final ProtinheritanceContext protinheritance() throws RecognitionException {
		ProtinheritanceContext _localctx = new ProtinheritanceContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_protinheritance);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(60);
			match(T__5);
			setState(61);
			((ProtinheritanceContext)_localctx).inh = match(ID);
			setState(66);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__6) {
				{
				{
				setState(62);
				match(T__6);
				setState(63);
				((ProtinheritanceContext)_localctx).inh = match(ID);
				}
				}
				setState(68);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(69);
			match(T__7);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InheritanceContext extends ParserRuleContext {
		public Token classname;
		public TerminalNode ID() { return getToken(CMAnnotParser.ID, 0); }
		public ProtinheritanceContext protinheritance() {
			return getRuleContext(ProtinheritanceContext.class,0);
		}
		public InheritanceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_inheritance; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CMAnnotListener ) ((CMAnnotListener)listener).enterInheritance(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CMAnnotListener ) ((CMAnnotListener)listener).exitInheritance(this);
		}
	}

	public final InheritanceContext inheritance() throws RecognitionException {
		InheritanceContext _localctx = new InheritanceContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_inheritance);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(71);
			match(T__8);
			setState(72);
			((InheritanceContext)_localctx).classname = match(ID);
			setState(74);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__5) {
				{
				setState(73);
				protinheritance();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MethodContext extends ParserRuleContext {
		public PropertyContext property() {
			return getRuleContext(PropertyContext.class,0);
		}
		public SelectorContext selector() {
			return getRuleContext(SelectorContext.class,0);
		}
		public MethodContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_method; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CMAnnotListener ) ((CMAnnotListener)listener).enterMethod(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CMAnnotListener ) ((CMAnnotListener)listener).exitMethod(this);
		}
	}

	public final MethodContext method() throws RecognitionException {
		MethodContext _localctx = new MethodContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_method);
		try {
			setState(78);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__9:
			case T__24:
			case T__31:
			case ID:
				enterOuterAlt(_localctx, 1);
				{
				setState(76);
				property();
				}
				break;
			case T__26:
			case T__27:
				enterOuterAlt(_localctx, 2);
				{
				setState(77);
				selector();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PropertyContext extends ParserRuleContext {
		public Token objc;
		public VariableContext variable() {
			return getRuleContext(VariableContext.class,0);
		}
		public List<PropertydefContext> propertydef() {
			return getRuleContexts(PropertydefContext.class);
		}
		public PropertydefContext propertydef(int i) {
			return getRuleContext(PropertydefContext.class,i);
		}
		public PropertyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_property; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CMAnnotListener ) ((CMAnnotListener)listener).enterProperty(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CMAnnotListener ) ((CMAnnotListener)listener).exitProperty(this);
		}
	}

	public final PropertyContext property() throws RecognitionException {
		PropertyContext _localctx = new PropertyContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_property);
		int _la;
		try {
			setState(101);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(80);
				((PropertyContext)_localctx).objc = match(T__9);
				setState(81);
				variable();
				setState(82);
				match(T__10);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(84);
				((PropertyContext)_localctx).objc = match(T__9);
				setState(85);
				match(T__2);
				setState(86);
				propertydef();
				setState(91);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__6) {
					{
					{
					setState(87);
					match(T__6);
					setState(88);
					propertydef();
					}
					}
					setState(93);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(94);
				match(T__3);
				setState(95);
				variable();
				setState(96);
				match(T__10);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(98);
				variable();
				setState(99);
				match(T__10);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PropertydefContext extends ParserRuleContext {
		public Token clazz;
		public Token strong;
		public Token weak;
		public Token copy;
		public Token readonly;
		public Token getter;
		public TerminalNode ID() { return getToken(CMAnnotParser.ID, 0); }
		public PropertydefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_propertydef; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CMAnnotListener ) ((CMAnnotListener)listener).enterPropertydef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CMAnnotListener ) ((CMAnnotListener)listener).exitPropertydef(this);
		}
	}

	public final PropertydefContext propertydef() throws RecognitionException {
		PropertydefContext _localctx = new PropertydefContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_propertydef);
		try {
			setState(117);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__11:
				enterOuterAlt(_localctx, 1);
				{
				setState(103);
				match(T__11);
				}
				break;
			case T__12:
				enterOuterAlt(_localctx, 2);
				{
				setState(104);
				match(T__12);
				}
				break;
			case T__13:
				enterOuterAlt(_localctx, 3);
				{
				setState(105);
				match(T__13);
				}
				break;
			case T__14:
				enterOuterAlt(_localctx, 4);
				{
				setState(106);
				match(T__14);
				}
				break;
			case T__15:
				enterOuterAlt(_localctx, 5);
				{
				setState(107);
				((PropertydefContext)_localctx).clazz = match(T__15);
				}
				break;
			case T__16:
				enterOuterAlt(_localctx, 6);
				{
				setState(108);
				((PropertydefContext)_localctx).strong = match(T__16);
				}
				break;
			case T__17:
				enterOuterAlt(_localctx, 7);
				{
				setState(109);
				((PropertydefContext)_localctx).strong = match(T__17);
				}
				break;
			case T__18:
				enterOuterAlt(_localctx, 8);
				{
				setState(110);
				((PropertydefContext)_localctx).weak = match(T__18);
				}
				break;
			case T__19:
				enterOuterAlt(_localctx, 9);
				{
				setState(111);
				((PropertydefContext)_localctx).weak = match(T__19);
				}
				break;
			case T__20:
				enterOuterAlt(_localctx, 10);
				{
				setState(112);
				((PropertydefContext)_localctx).copy = match(T__20);
				}
				break;
			case T__21:
				enterOuterAlt(_localctx, 11);
				{
				setState(113);
				((PropertydefContext)_localctx).readonly = match(T__21);
				}
				break;
			case T__22:
				enterOuterAlt(_localctx, 12);
				{
				setState(114);
				match(T__22);
				setState(115);
				match(T__23);
				setState(116);
				((PropertydefContext)_localctx).getter = match(ID);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CpartsContext extends ParserRuleContext {
		public FuncContext func() {
			return getRuleContext(FuncContext.class,0);
		}
		public ExtconstContext extconst() {
			return getRuleContext(ExtconstContext.class,0);
		}
		public CpartsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cparts; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CMAnnotListener ) ((CMAnnotListener)listener).enterCparts(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CMAnnotListener ) ((CMAnnotListener)listener).exitCparts(this);
		}
	}

	public final CpartsContext cparts() throws RecognitionException {
		CpartsContext _localctx = new CpartsContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_cparts);
		try {
			setState(121);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(119);
				func();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(120);
				extconst();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FuncContext extends ParserRuleContext {
		public Token constant;
		public Token name;
		public VartypeContext vartype() {
			return getRuleContext(VartypeContext.class,0);
		}
		public ListofvariablesContext listofvariables() {
			return getRuleContext(ListofvariablesContext.class,0);
		}
		public TerminalNode ID() { return getToken(CMAnnotParser.ID, 0); }
		public FuncContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_func; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CMAnnotListener ) ((CMAnnotListener)listener).enterFunc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CMAnnotListener ) ((CMAnnotListener)listener).exitFunc(this);
		}
	}

	public final FuncContext func() throws RecognitionException {
		FuncContext _localctx = new FuncContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_func);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(124);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
			case 1:
				{
				setState(123);
				((FuncContext)_localctx).constant = match(T__24);
				}
				break;
			}
			setState(126);
			vartype();
			setState(127);
			((FuncContext)_localctx).name = match(ID);
			setState(128);
			match(T__2);
			setState(129);
			listofvariables();
			setState(130);
			match(T__3);
			setState(131);
			match(T__10);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExtconstContext extends ParserRuleContext {
		public Token constant;
		public Token name;
		public VartypeContext vartype() {
			return getRuleContext(VartypeContext.class,0);
		}
		public TerminalNode ID() { return getToken(CMAnnotParser.ID, 0); }
		public ExtconstContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_extconst; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CMAnnotListener ) ((CMAnnotListener)listener).enterExtconst(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CMAnnotListener ) ((CMAnnotListener)listener).exitExtconst(this);
		}
	}

	public final ExtconstContext extconst() throws RecognitionException {
		ExtconstContext _localctx = new ExtconstContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_extconst);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(134);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
			case 1:
				{
				setState(133);
				((ExtconstContext)_localctx).constant = match(T__24);
				}
				break;
			}
			setState(136);
			vartype();
			setState(137);
			((ExtconstContext)_localctx).name = match(ID);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SelectorContext extends ParserRuleContext {
		public Token name;
		public Token retain;
		public Token varargs;
		public ReturntypeContext returntype() {
			return getRuleContext(ReturntypeContext.class,0);
		}
		public TerminalNode ID() { return getToken(CMAnnotParser.ID, 0); }
		public SelectorParamContext selectorParam() {
			return getRuleContext(SelectorParamContext.class,0);
		}
		public List<SelectorNamedParamContext> selectorNamedParam() {
			return getRuleContexts(SelectorNamedParamContext.class);
		}
		public SelectorNamedParamContext selectorNamedParam(int i) {
			return getRuleContext(SelectorNamedParamContext.class,i);
		}
		public SelectorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_selector; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CMAnnotListener ) ((CMAnnotListener)listener).enterSelector(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CMAnnotListener ) ((CMAnnotListener)listener).exitSelector(this);
		}
	}

	public final SelectorContext selector() throws RecognitionException {
		SelectorContext _localctx = new SelectorContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_selector);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(139);
			returntype();
			setState(142);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ID:
				{
				setState(140);
				((SelectorContext)_localctx).name = match(ID);
				}
				break;
			case T__16:
				{
				setState(141);
				((SelectorContext)_localctx).retain = match(T__16);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(151);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__8) {
				{
				setState(144);
				selectorParam();
				setState(148);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__8 || _la==ID) {
					{
					{
					setState(145);
					selectorNamedParam();
					}
					}
					setState(150);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(155);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__6) {
				{
				setState(153);
				match(T__6);
				setState(154);
				((SelectorContext)_localctx).varargs = match(T__25);
				}
			}

			setState(157);
			match(T__10);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SelectorNamedParamContext extends ParserRuleContext {
		public Token paramname;
		public SelectorParamContext selectorparam;
		public SelectorParamContext selectorParam() {
			return getRuleContext(SelectorParamContext.class,0);
		}
		public TerminalNode ID() { return getToken(CMAnnotParser.ID, 0); }
		public SelectorNamedParamContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_selectorNamedParam; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CMAnnotListener ) ((CMAnnotListener)listener).enterSelectorNamedParam(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CMAnnotListener ) ((CMAnnotListener)listener).exitSelectorNamedParam(this);
		}
	}

	public final SelectorNamedParamContext selectorNamedParam() throws RecognitionException {
		SelectorNamedParamContext _localctx = new SelectorNamedParamContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_selectorNamedParam);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(160);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ID) {
				{
				setState(159);
				((SelectorNamedParamContext)_localctx).paramname = match(ID);
				}
			}

			setState(162);
			((SelectorNamedParamContext)_localctx).selectorparam = selectorParam();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SelectorParamContext extends ParserRuleContext {
		public Token variablename;
		public VartypeContext vartype() {
			return getRuleContext(VartypeContext.class,0);
		}
		public TerminalNode ID() { return getToken(CMAnnotParser.ID, 0); }
		public SelectorParamContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_selectorParam; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CMAnnotListener ) ((CMAnnotListener)listener).enterSelectorParam(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CMAnnotListener ) ((CMAnnotListener)listener).exitSelectorParam(this);
		}
	}

	public final SelectorParamContext selectorParam() throws RecognitionException {
		SelectorParamContext _localctx = new SelectorParamContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_selectorParam);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(164);
			match(T__8);
			setState(165);
			match(T__2);
			setState(166);
			vartype();
			setState(167);
			match(T__3);
			setState(168);
			((SelectorParamContext)_localctx).variablename = match(ID);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ReturntypeContext extends ParserRuleContext {
		public Token isstatic;
		public VartypeContext vartype() {
			return getRuleContext(VartypeContext.class,0);
		}
		public ReturntypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_returntype; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CMAnnotListener ) ((CMAnnotListener)listener).enterReturntype(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CMAnnotListener ) ((CMAnnotListener)listener).exitReturntype(this);
		}
	}

	public final ReturntypeContext returntype() throws RecognitionException {
		ReturntypeContext _localctx = new ReturntypeContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_returntype);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(170);
			((ReturntypeContext)_localctx).isstatic = _input.LT(1);
			_la = _input.LA(1);
			if ( !(_la==T__26 || _la==T__27) ) {
				((ReturntypeContext)_localctx).isstatic = (Token)_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(171);
			match(T__2);
			setState(172);
			vartype();
			setState(173);
			match(T__3);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ListofvariablesContext extends ParserRuleContext {
		public Token voidTK;
		public List<Var_type_nameContext> var_type_name() {
			return getRuleContexts(Var_type_nameContext.class);
		}
		public Var_type_nameContext var_type_name(int i) {
			return getRuleContext(Var_type_nameContext.class,i);
		}
		public TerminalNode ID() { return getToken(CMAnnotParser.ID, 0); }
		public ListofvariablesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_listofvariables; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CMAnnotListener ) ((CMAnnotListener)listener).enterListofvariables(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CMAnnotListener ) ((CMAnnotListener)listener).exitListofvariables(this);
		}
	}

	public final ListofvariablesContext listofvariables() throws RecognitionException {
		ListofvariablesContext _localctx = new ListofvariablesContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_listofvariables);
		int _la;
		try {
			setState(185);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,18,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(175);
				var_type_name();
				setState(180);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__6) {
					{
					{
					setState(176);
					match(T__6);
					setState(177);
					var_type_name();
					}
					}
					setState(182);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(183);
				((ListofvariablesContext)_localctx).voidTK = match(ID);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Var_type_nameContext extends ParserRuleContext {
		public Token varname;
		public Token s3;
		public Token varargs;
		public VartypeContext vartype() {
			return getRuleContext(VartypeContext.class,0);
		}
		public TerminalNode ID() { return getToken(CMAnnotParser.ID, 0); }
		public Var_type_nameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_var_type_name; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CMAnnotListener ) ((CMAnnotListener)listener).enterVar_type_name(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CMAnnotListener ) ((CMAnnotListener)listener).exitVar_type_name(this);
		}
	}

	public final Var_type_nameContext var_type_name() throws RecognitionException {
		Var_type_nameContext _localctx = new Var_type_nameContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_var_type_name);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(187);
			vartype();
			setState(188);
			((Var_type_nameContext)_localctx).varname = match(ID);
			setState(191);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__28) {
				{
				setState(189);
				((Var_type_nameContext)_localctx).s3 = match(T__28);
				setState(190);
				match(T__29);
				}
			}

			setState(195);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,20,_ctx) ) {
			case 1:
				{
				setState(193);
				match(T__6);
				setState(194);
				((Var_type_nameContext)_localctx).varargs = match(T__25);
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VariableContext extends ParserRuleContext {
		public VartypeContext vartype() {
			return getRuleContext(VartypeContext.class,0);
		}
		public TerminalNode ID() { return getToken(CMAnnotParser.ID, 0); }
		public VariableContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variable; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CMAnnotListener ) ((CMAnnotListener)listener).enterVariable(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CMAnnotListener ) ((CMAnnotListener)listener).exitVariable(this);
		}
	}

	public final VariableContext variable() throws RecognitionException {
		VariableContext _localctx = new VariableContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_variable);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(197);
			vartype();
			setState(198);
			match(ID);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VartypeContext extends ParserRuleContext {
		public Token constant;
		public Token type;
		public Token s1;
		public Token s2;
		public Token protocol;
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public List<VartypeContext> vartype() {
			return getRuleContexts(VartypeContext.class);
		}
		public VartypeContext vartype(int i) {
			return getRuleContext(VartypeContext.class,i);
		}
		public List<TerminalNode> ID() { return getTokens(CMAnnotParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(CMAnnotParser.ID, i);
		}
		public VartypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_vartype; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CMAnnotListener ) ((CMAnnotListener)listener).enterVartype(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CMAnnotListener ) ((CMAnnotListener)listener).exitVartype(this);
		}
	}

	public final VartypeContext vartype() throws RecognitionException {
		VartypeContext _localctx = new VartypeContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_vartype);
		int _la;
		try {
			setState(244);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,30,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(200);
				block();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(202);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__24) {
					{
					setState(201);
					((VartypeContext)_localctx).constant = match(T__24);
					}
				}

				setState(204);
				((VartypeContext)_localctx).type = match(ID);
				setState(205);
				match(T__5);
				setState(206);
				vartype();
				setState(211);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__6) {
					{
					{
					setState(207);
					match(T__6);
					setState(208);
					vartype();
					}
					}
					setState(213);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(214);
				match(T__7);
				setState(216);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,23,_ctx) ) {
				case 1:
					{
					setState(215);
					((VartypeContext)_localctx).s1 = match(T__30);
					}
					break;
				}
				setState(219);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__30) {
					{
					setState(218);
					((VartypeContext)_localctx).s2 = match(T__30);
					}
				}

				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(222);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__24) {
					{
					setState(221);
					((VartypeContext)_localctx).constant = match(T__24);
					}
				}

				setState(224);
				((VartypeContext)_localctx).type = match(ID);
				setState(226);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,26,_ctx) ) {
				case 1:
					{
					setState(225);
					match(ID);
					}
					break;
				}
				setState(229);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,27,_ctx) ) {
				case 1:
					{
					setState(228);
					((VartypeContext)_localctx).s1 = match(T__30);
					}
					break;
				}
				setState(232);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__30) {
					{
					setState(231);
					((VartypeContext)_localctx).s2 = match(T__30);
					}
				}

				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(234);
				match(T__31);
				setState(235);
				((VartypeContext)_localctx).protocol = match(ID);
				setState(240);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__6) {
					{
					{
					setState(236);
					match(T__6);
					setState(237);
					((VartypeContext)_localctx).protocol = match(ID);
					}
					}
					setState(242);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(243);
				match(T__7);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BlockContext extends ParserRuleContext {
		public Token constant;
		public List<Simple_vartype_nameContext> simple_vartype_name() {
			return getRuleContexts(Simple_vartype_nameContext.class);
		}
		public Simple_vartype_nameContext simple_vartype_name(int i) {
			return getRuleContext(Simple_vartype_nameContext.class,i);
		}
		public TerminalNode ID() { return getToken(CMAnnotParser.ID, 0); }
		public BlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_block; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CMAnnotListener ) ((CMAnnotListener)listener).enterBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CMAnnotListener ) ((CMAnnotListener)listener).exitBlock(this);
		}
	}

	public final BlockContext block() throws RecognitionException {
		BlockContext _localctx = new BlockContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_block);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(247);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,31,_ctx) ) {
			case 1:
				{
				setState(246);
				((BlockContext)_localctx).constant = match(T__24);
				}
				break;
			}
			setState(249);
			simple_vartype_name();
			setState(250);
			match(T__2);
			setState(251);
			match(T__32);
			setState(253);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ID) {
				{
				setState(252);
				match(ID);
				}
			}

			setState(255);
			match(T__3);
			setState(256);
			match(T__2);
			setState(257);
			simple_vartype_name();
			setState(262);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__6) {
				{
				{
				setState(258);
				match(T__6);
				setState(259);
				simple_vartype_name();
				}
				}
				setState(264);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(265);
			match(T__3);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Simple_vartype_nameContext extends ParserRuleContext {
		public Token name;
		public Simple_vartypeContext simple_vartype() {
			return getRuleContext(Simple_vartypeContext.class,0);
		}
		public TerminalNode ID() { return getToken(CMAnnotParser.ID, 0); }
		public Simple_vartype_nameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_simple_vartype_name; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CMAnnotListener ) ((CMAnnotListener)listener).enterSimple_vartype_name(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CMAnnotListener ) ((CMAnnotListener)listener).exitSimple_vartype_name(this);
		}
	}

	public final Simple_vartype_nameContext simple_vartype_name() throws RecognitionException {
		Simple_vartype_nameContext _localctx = new Simple_vartype_nameContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_simple_vartype_name);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(267);
			simple_vartype();
			setState(269);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ID) {
				{
				setState(268);
				((Simple_vartype_nameContext)_localctx).name = match(ID);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Simple_vartypeContext extends ParserRuleContext {
		public Token constant;
		public Token type;
		public Token s1;
		public Token s2;
		public List<Simple_vartypeContext> simple_vartype() {
			return getRuleContexts(Simple_vartypeContext.class);
		}
		public Simple_vartypeContext simple_vartype(int i) {
			return getRuleContext(Simple_vartypeContext.class,i);
		}
		public TerminalNode ID() { return getToken(CMAnnotParser.ID, 0); }
		public Simple_vartypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_simple_vartype; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CMAnnotListener ) ((CMAnnotListener)listener).enterSimple_vartype(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CMAnnotListener ) ((CMAnnotListener)listener).exitSimple_vartype(this);
		}
	}

	public final Simple_vartypeContext simple_vartype() throws RecognitionException {
		Simple_vartypeContext _localctx = new Simple_vartypeContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_simple_vartype);
		int _la;
		try {
			setState(304);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,42,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(272);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__24) {
					{
					setState(271);
					((Simple_vartypeContext)_localctx).constant = match(T__24);
					}
				}

				setState(274);
				((Simple_vartypeContext)_localctx).type = match(ID);
				setState(275);
				match(T__5);
				setState(276);
				simple_vartype();
				setState(281);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__6) {
					{
					{
					setState(277);
					match(T__6);
					setState(278);
					simple_vartype();
					}
					}
					setState(283);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(284);
				match(T__7);
				setState(286);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,37,_ctx) ) {
				case 1:
					{
					setState(285);
					((Simple_vartypeContext)_localctx).s1 = match(T__30);
					}
					break;
				}
				setState(289);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__30) {
					{
					setState(288);
					((Simple_vartypeContext)_localctx).s2 = match(T__30);
					}
				}

				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(292);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__24) {
					{
					setState(291);
					((Simple_vartypeContext)_localctx).constant = match(T__24);
					}
				}

				setState(294);
				match(T__31);
				setState(295);
				((Simple_vartypeContext)_localctx).type = match(ID);
				setState(296);
				match(T__7);
				setState(298);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__30) {
					{
					setState(297);
					((Simple_vartypeContext)_localctx).s1 = match(T__30);
					}
				}

				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(300);
				((Simple_vartypeContext)_localctx).type = match(ID);
				setState(302);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__30) {
					{
					setState(301);
					((Simple_vartypeContext)_localctx).s1 = match(T__30);
					}
				}

				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VisibilityContext extends ParserRuleContext {
		public VisibilityContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_visibility; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CMAnnotListener ) ((CMAnnotListener)listener).enterVisibility(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CMAnnotListener ) ((CMAnnotListener)listener).exitVisibility(this);
		}
	}

	public final VisibilityContext visibility() throws RecognitionException {
		VisibilityContext _localctx = new VisibilityContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_visibility);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(306);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__33) | (1L << T__34) | (1L << T__35) | (1L << T__36))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\65\u0137\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\3\2\3\2\3\2\3\2\3\2\5\2\62\n"+
		"\2\3\2\5\2\65\n\2\3\2\7\28\n\2\f\2\16\2;\13\2\3\2\3\2\3\3\3\3\3\3\3\3"+
		"\7\3C\n\3\f\3\16\3F\13\3\3\3\3\3\3\4\3\4\3\4\5\4M\n\4\3\5\3\5\5\5Q\n\5"+
		"\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\7\6\\\n\6\f\6\16\6_\13\6\3\6\3\6"+
		"\3\6\3\6\3\6\3\6\3\6\5\6h\n\6\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7"+
		"\3\7\3\7\3\7\3\7\5\7x\n\7\3\b\3\b\5\b|\n\b\3\t\5\t\177\n\t\3\t\3\t\3\t"+
		"\3\t\3\t\3\t\3\t\3\n\5\n\u0089\n\n\3\n\3\n\3\n\3\13\3\13\3\13\5\13\u0091"+
		"\n\13\3\13\3\13\7\13\u0095\n\13\f\13\16\13\u0098\13\13\5\13\u009a\n\13"+
		"\3\13\3\13\5\13\u009e\n\13\3\13\3\13\3\f\5\f\u00a3\n\f\3\f\3\f\3\r\3\r"+
		"\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\16\3\17\3\17\3\17\7\17\u00b5\n"+
		"\17\f\17\16\17\u00b8\13\17\3\17\3\17\5\17\u00bc\n\17\3\20\3\20\3\20\3"+
		"\20\5\20\u00c2\n\20\3\20\3\20\5\20\u00c6\n\20\3\21\3\21\3\21\3\22\3\22"+
		"\5\22\u00cd\n\22\3\22\3\22\3\22\3\22\3\22\7\22\u00d4\n\22\f\22\16\22\u00d7"+
		"\13\22\3\22\3\22\5\22\u00db\n\22\3\22\5\22\u00de\n\22\3\22\5\22\u00e1"+
		"\n\22\3\22\3\22\5\22\u00e5\n\22\3\22\5\22\u00e8\n\22\3\22\5\22\u00eb\n"+
		"\22\3\22\3\22\3\22\3\22\7\22\u00f1\n\22\f\22\16\22\u00f4\13\22\3\22\5"+
		"\22\u00f7\n\22\3\23\5\23\u00fa\n\23\3\23\3\23\3\23\3\23\5\23\u0100\n\23"+
		"\3\23\3\23\3\23\3\23\3\23\7\23\u0107\n\23\f\23\16\23\u010a\13\23\3\23"+
		"\3\23\3\24\3\24\5\24\u0110\n\24\3\25\5\25\u0113\n\25\3\25\3\25\3\25\3"+
		"\25\3\25\7\25\u011a\n\25\f\25\16\25\u011d\13\25\3\25\3\25\5\25\u0121\n"+
		"\25\3\25\5\25\u0124\n\25\3\25\5\25\u0127\n\25\3\25\3\25\3\25\3\25\5\25"+
		"\u012d\n\25\3\25\3\25\5\25\u0131\n\25\5\25\u0133\n\25\3\26\3\26\3\26\2"+
		"\2\27\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*\2\5\3\2\3\4\3\2\35"+
		"\36\3\2$\'\2\u015b\2,\3\2\2\2\4>\3\2\2\2\6I\3\2\2\2\bP\3\2\2\2\ng\3\2"+
		"\2\2\fw\3\2\2\2\16{\3\2\2\2\20~\3\2\2\2\22\u0088\3\2\2\2\24\u008d\3\2"+
		"\2\2\26\u00a2\3\2\2\2\30\u00a6\3\2\2\2\32\u00ac\3\2\2\2\34\u00bb\3\2\2"+
		"\2\36\u00bd\3\2\2\2 \u00c7\3\2\2\2\"\u00f6\3\2\2\2$\u00f9\3\2\2\2&\u010d"+
		"\3\2\2\2(\u0132\3\2\2\2*\u0134\3\2\2\2,-\t\2\2\2-\61\7\63\2\2./\7\5\2"+
		"\2/\60\7\63\2\2\60\62\7\6\2\2\61.\3\2\2\2\61\62\3\2\2\2\62\64\3\2\2\2"+
		"\63\65\5\6\4\2\64\63\3\2\2\2\64\65\3\2\2\2\659\3\2\2\2\668\5\b\5\2\67"+
		"\66\3\2\2\28;\3\2\2\29\67\3\2\2\29:\3\2\2\2:<\3\2\2\2;9\3\2\2\2<=\7\7"+
		"\2\2=\3\3\2\2\2>?\7\b\2\2?D\7\63\2\2@A\7\t\2\2AC\7\63\2\2B@\3\2\2\2CF"+
		"\3\2\2\2DB\3\2\2\2DE\3\2\2\2EG\3\2\2\2FD\3\2\2\2GH\7\n\2\2H\5\3\2\2\2"+
		"IJ\7\13\2\2JL\7\63\2\2KM\5\4\3\2LK\3\2\2\2LM\3\2\2\2M\7\3\2\2\2NQ\5\n"+
		"\6\2OQ\5\24\13\2PN\3\2\2\2PO\3\2\2\2Q\t\3\2\2\2RS\7\f\2\2ST\5 \21\2TU"+
		"\7\r\2\2Uh\3\2\2\2VW\7\f\2\2WX\7\5\2\2X]\5\f\7\2YZ\7\t\2\2Z\\\5\f\7\2"+
		"[Y\3\2\2\2\\_\3\2\2\2][\3\2\2\2]^\3\2\2\2^`\3\2\2\2_]\3\2\2\2`a\7\6\2"+
		"\2ab\5 \21\2bc\7\r\2\2ch\3\2\2\2de\5 \21\2ef\7\r\2\2fh\3\2\2\2gR\3\2\2"+
		"\2gV\3\2\2\2gd\3\2\2\2h\13\3\2\2\2ix\7\16\2\2jx\7\17\2\2kx\7\20\2\2lx"+
		"\7\21\2\2mx\7\22\2\2nx\7\23\2\2ox\7\24\2\2px\7\25\2\2qx\7\26\2\2rx\7\27"+
		"\2\2sx\7\30\2\2tu\7\31\2\2uv\7\32\2\2vx\7\63\2\2wi\3\2\2\2wj\3\2\2\2w"+
		"k\3\2\2\2wl\3\2\2\2wm\3\2\2\2wn\3\2\2\2wo\3\2\2\2wp\3\2\2\2wq\3\2\2\2"+
		"wr\3\2\2\2ws\3\2\2\2wt\3\2\2\2x\r\3\2\2\2y|\5\20\t\2z|\5\22\n\2{y\3\2"+
		"\2\2{z\3\2\2\2|\17\3\2\2\2}\177\7\33\2\2~}\3\2\2\2~\177\3\2\2\2\177\u0080"+
		"\3\2\2\2\u0080\u0081\5\"\22\2\u0081\u0082\7\63\2\2\u0082\u0083\7\5\2\2"+
		"\u0083\u0084\5\34\17\2\u0084\u0085\7\6\2\2\u0085\u0086\7\r\2\2\u0086\21"+
		"\3\2\2\2\u0087\u0089\7\33\2\2\u0088\u0087\3\2\2\2\u0088\u0089\3\2\2\2"+
		"\u0089\u008a\3\2\2\2\u008a\u008b\5\"\22\2\u008b\u008c\7\63\2\2\u008c\23"+
		"\3\2\2\2\u008d\u0090\5\32\16\2\u008e\u0091\7\63\2\2\u008f\u0091\7\23\2"+
		"\2\u0090\u008e\3\2\2\2\u0090\u008f\3\2\2\2\u0091\u0099\3\2\2\2\u0092\u0096"+
		"\5\30\r\2\u0093\u0095\5\26\f\2\u0094\u0093\3\2\2\2\u0095\u0098\3\2\2\2"+
		"\u0096\u0094\3\2\2\2\u0096\u0097\3\2\2\2\u0097\u009a\3\2\2\2\u0098\u0096"+
		"\3\2\2\2\u0099\u0092\3\2\2\2\u0099\u009a\3\2\2\2\u009a\u009d\3\2\2\2\u009b"+
		"\u009c\7\t\2\2\u009c\u009e\7\34\2\2\u009d\u009b\3\2\2\2\u009d\u009e\3"+
		"\2\2\2\u009e\u009f\3\2\2\2\u009f\u00a0\7\r\2\2\u00a0\25\3\2\2\2\u00a1"+
		"\u00a3\7\63\2\2\u00a2\u00a1\3\2\2\2\u00a2\u00a3\3\2\2\2\u00a3\u00a4\3"+
		"\2\2\2\u00a4\u00a5\5\30\r\2\u00a5\27\3\2\2\2\u00a6\u00a7\7\13\2\2\u00a7"+
		"\u00a8\7\5\2\2\u00a8\u00a9\5\"\22\2\u00a9\u00aa\7\6\2\2\u00aa\u00ab\7"+
		"\63\2\2\u00ab\31\3\2\2\2\u00ac\u00ad\t\3\2\2\u00ad\u00ae\7\5\2\2\u00ae"+
		"\u00af\5\"\22\2\u00af\u00b0\7\6\2\2\u00b0\33\3\2\2\2\u00b1\u00b6\5\36"+
		"\20\2\u00b2\u00b3\7\t\2\2\u00b3\u00b5\5\36\20\2\u00b4\u00b2\3\2\2\2\u00b5"+
		"\u00b8\3\2\2\2\u00b6\u00b4\3\2\2\2\u00b6\u00b7\3\2\2\2\u00b7\u00bc\3\2"+
		"\2\2\u00b8\u00b6\3\2\2\2\u00b9\u00bc\7\63\2\2\u00ba\u00bc\3\2\2\2\u00bb"+
		"\u00b1\3\2\2\2\u00bb\u00b9\3\2\2\2\u00bb\u00ba\3\2\2\2\u00bc\35\3\2\2"+
		"\2\u00bd\u00be\5\"\22\2\u00be\u00c1\7\63\2\2\u00bf\u00c0\7\37\2\2\u00c0"+
		"\u00c2\7 \2\2\u00c1\u00bf\3\2\2\2\u00c1\u00c2\3\2\2\2\u00c2\u00c5\3\2"+
		"\2\2\u00c3\u00c4\7\t\2\2\u00c4\u00c6\7\34\2\2\u00c5\u00c3\3\2\2\2\u00c5"+
		"\u00c6\3\2\2\2\u00c6\37\3\2\2\2\u00c7\u00c8\5\"\22\2\u00c8\u00c9\7\63"+
		"\2\2\u00c9!\3\2\2\2\u00ca\u00f7\5$\23\2\u00cb\u00cd\7\33\2\2\u00cc\u00cb"+
		"\3\2\2\2\u00cc\u00cd\3\2\2\2\u00cd\u00ce\3\2\2\2\u00ce\u00cf\7\63\2\2"+
		"\u00cf\u00d0\7\b\2\2\u00d0\u00d5\5\"\22\2\u00d1\u00d2\7\t\2\2\u00d2\u00d4"+
		"\5\"\22\2\u00d3\u00d1\3\2\2\2\u00d4\u00d7\3\2\2\2\u00d5\u00d3\3\2\2\2"+
		"\u00d5\u00d6\3\2\2\2\u00d6\u00d8\3\2\2\2\u00d7\u00d5\3\2\2\2\u00d8\u00da"+
		"\7\n\2\2\u00d9\u00db\7!\2\2\u00da\u00d9\3\2\2\2\u00da\u00db\3\2\2\2\u00db"+
		"\u00dd\3\2\2\2\u00dc\u00de\7!\2\2\u00dd\u00dc\3\2\2\2\u00dd\u00de\3\2"+
		"\2\2\u00de\u00f7\3\2\2\2\u00df\u00e1\7\33\2\2\u00e0\u00df\3\2\2\2\u00e0"+
		"\u00e1\3\2\2\2\u00e1\u00e2\3\2\2\2\u00e2\u00e4\7\63\2\2\u00e3\u00e5\7"+
		"\63\2\2\u00e4\u00e3\3\2\2\2\u00e4\u00e5\3\2\2\2\u00e5\u00e7\3\2\2\2\u00e6"+
		"\u00e8\7!\2\2\u00e7\u00e6\3\2\2\2\u00e7\u00e8\3\2\2\2\u00e8\u00ea\3\2"+
		"\2\2\u00e9\u00eb\7!\2\2\u00ea\u00e9\3\2\2\2\u00ea\u00eb\3\2\2\2\u00eb"+
		"\u00f7\3\2\2\2\u00ec\u00ed\7\"\2\2\u00ed\u00f2\7\63\2\2\u00ee\u00ef\7"+
		"\t\2\2\u00ef\u00f1\7\63\2\2\u00f0\u00ee\3\2\2\2\u00f1\u00f4\3\2\2\2\u00f2"+
		"\u00f0\3\2\2\2\u00f2\u00f3\3\2\2\2\u00f3\u00f5\3\2\2\2\u00f4\u00f2\3\2"+
		"\2\2\u00f5\u00f7\7\n\2\2\u00f6\u00ca\3\2\2\2\u00f6\u00cc\3\2\2\2\u00f6"+
		"\u00e0\3\2\2\2\u00f6\u00ec\3\2\2\2\u00f7#\3\2\2\2\u00f8\u00fa\7\33\2\2"+
		"\u00f9\u00f8\3\2\2\2\u00f9\u00fa\3\2\2\2\u00fa\u00fb\3\2\2\2\u00fb\u00fc"+
		"\5&\24\2\u00fc\u00fd\7\5\2\2\u00fd\u00ff\7#\2\2\u00fe\u0100\7\63\2\2\u00ff"+
		"\u00fe\3\2\2\2\u00ff\u0100\3\2\2\2\u0100\u0101\3\2\2\2\u0101\u0102\7\6"+
		"\2\2\u0102\u0103\7\5\2\2\u0103\u0108\5&\24\2\u0104\u0105\7\t\2\2\u0105"+
		"\u0107\5&\24\2\u0106\u0104\3\2\2\2\u0107\u010a\3\2\2\2\u0108\u0106\3\2"+
		"\2\2\u0108\u0109\3\2\2\2\u0109\u010b\3\2\2\2\u010a\u0108\3\2\2\2\u010b"+
		"\u010c\7\6\2\2\u010c%\3\2\2\2\u010d\u010f\5(\25\2\u010e\u0110\7\63\2\2"+
		"\u010f\u010e\3\2\2\2\u010f\u0110\3\2\2\2\u0110\'\3\2\2\2\u0111\u0113\7"+
		"\33\2\2\u0112\u0111\3\2\2\2\u0112\u0113\3\2\2\2\u0113\u0114\3\2\2\2\u0114"+
		"\u0115\7\63\2\2\u0115\u0116\7\b\2\2\u0116\u011b\5(\25\2\u0117\u0118\7"+
		"\t\2\2\u0118\u011a\5(\25\2\u0119\u0117\3\2\2\2\u011a\u011d\3\2\2\2\u011b"+
		"\u0119\3\2\2\2\u011b\u011c\3\2\2\2\u011c\u011e\3\2\2\2\u011d\u011b\3\2"+
		"\2\2\u011e\u0120\7\n\2\2\u011f\u0121\7!\2\2\u0120\u011f\3\2\2\2\u0120"+
		"\u0121\3\2\2\2\u0121\u0123\3\2\2\2\u0122\u0124\7!\2\2\u0123\u0122\3\2"+
		"\2\2\u0123\u0124\3\2\2\2\u0124\u0133\3\2\2\2\u0125\u0127\7\33\2\2\u0126"+
		"\u0125\3\2\2\2\u0126\u0127\3\2\2\2\u0127\u0128\3\2\2\2\u0128\u0129\7\""+
		"\2\2\u0129\u012a\7\63\2\2\u012a\u012c\7\n\2\2\u012b\u012d\7!\2\2\u012c"+
		"\u012b\3\2\2\2\u012c\u012d\3\2\2\2\u012d\u0133\3\2\2\2\u012e\u0130\7\63"+
		"\2\2\u012f\u0131\7!\2\2\u0130\u012f\3\2\2\2\u0130\u0131\3\2\2\2\u0131"+
		"\u0133\3\2\2\2\u0132\u0112\3\2\2\2\u0132\u0126\3\2\2\2\u0132\u012e\3\2"+
		"\2\2\u0133)\3\2\2\2\u0134\u0135\t\4\2\2\u0135+\3\2\2\2-\61\649DLP]gw{"+
		"~\u0088\u0090\u0096\u0099\u009d\u00a2\u00b6\u00bb\u00c1\u00c5\u00cc\u00d5"+
		"\u00da\u00dd\u00e0\u00e4\u00e7\u00ea\u00f2\u00f6\u00f9\u00ff\u0108\u010f"+
		"\u0112\u011b\u0120\u0123\u0126\u012c\u0130\u0132";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}
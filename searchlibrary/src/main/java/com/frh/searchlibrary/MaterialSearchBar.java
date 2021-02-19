package com.frh.searchlibrary;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.BoringLayout;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.frh.searchlibrary.databinding.SearchbarBinding;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class MaterialSearchBar extends FrameLayout implements View.OnClickListener,
        Animation.AnimationListener, SuggestionsAdapter.OnItemViewClickListener,
        View.OnFocusChangeListener, TextView.OnEditorActionListener {
    public static final int BUTTON_SPEECH = 1;
    public static final int BUTTON_NAVIGATION = 2;
    public static final int BUTTON_BACK = 3;
    public static final int VIEW_VISIBLE = 1;
    public static final int VIEW_INVISIBLE = 0;

    SearchbarBinding binding;

    private OnSearchActionListener onSearchActionListener;
    private boolean searchOpened;
    private boolean suggestionsVisible;
    private boolean isSuggestionsEnabled = true;
    private SuggestionsAdapter adapter;
    private float destiny;

    private PopupMenu popupMenu;

    private int navIconResId;
    private int menuIconRes;
    private int searchIconRes;
    private int speechIconRes;
    private int arrowIconRes;
    private int clearIconRes;

    private boolean speechMode;
    private int maxSuggestionCount;
    private boolean navButtonEnabled;
    private boolean roundedSearchBarEnabled;
    private int dividerColor;
    private int searchBarColor;

    private CharSequence hintText;
    private CharSequence placeholderText;
    private int textColor;
    private int hintColor;
    private int placeholderColor;
    private int navIconTint;
    private int menuIconTint;
    private int searchIconTint;
    private int arrowIconTint;
    private int clearIconTint;

    private boolean navIconTintEnabled;
    private boolean menuIconTintEnabled;
    private boolean searchIconTintEnabled;
    private boolean arrowIconTintEnabled;
    private boolean clearIconTintEnabled;
    private boolean borderlessRippleEnabled = false;

    private int textCursorColor;
    private int highlightedTextColor;

    public MaterialSearchBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MaterialSearchBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MaterialSearchBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.searchbar, this, true);

        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.MaterialSearchBar);

        //Base Attributes
        speechMode = array.getBoolean(R.styleable.MaterialSearchBar_mt_speechMode, false);
        maxSuggestionCount = array.getInt(R.styleable.MaterialSearchBar_mt_maxSuggestionsCount, 3);
        navButtonEnabled = array.getBoolean(R.styleable.MaterialSearchBar_mt_navIconEnabled, false);
        roundedSearchBarEnabled = array.getBoolean(R.styleable.MaterialSearchBar_mt_roundedSearchBarEnabled, false);
        dividerColor = array.getColor(R.styleable.MaterialSearchBar_mt_dividerColor, ContextCompat.getColor(getContext(), R.color.searchBarDividerColor));
        searchBarColor = array.getColor(R.styleable.MaterialSearchBar_mt_searchBarColor, ContextCompat.getColor(getContext(), R.color.searchBarPrimaryColor));

        //Icon Related Attributes
        menuIconRes = array.getResourceId(R.styleable.MaterialSearchBar_mt_menuIconDrawable, R.drawable.ic_dots_vertical_black_48dp);
        searchIconRes = array.getResourceId(R.styleable.MaterialSearchBar_mt_searchIconDrawable, R.drawable.ic_magnify_black_48dp);
        speechIconRes = array.getResourceId(R.styleable.MaterialSearchBar_mt_speechIconDrawable, R.drawable.ic_microphone_black_48dp);
        arrowIconRes = array.getResourceId(R.styleable.MaterialSearchBar_mt_backIconDrawable, R.drawable.ic_arrow_left_black_48dp);
        clearIconRes = array.getResourceId(R.styleable.MaterialSearchBar_mt_clearIconDrawable, R.drawable.ic_close_black_48dp);
        navIconTint = array.getColor(R.styleable.MaterialSearchBar_mt_navIconTint, ContextCompat.getColor(getContext(), R.color.searchBarNavIconTintColor));
        menuIconTint = array.getColor(R.styleable.MaterialSearchBar_mt_menuIconTint, ContextCompat.getColor(getContext(), R.color.searchBarMenuIconTintColor));
        searchIconTint = array.getColor(R.styleable.MaterialSearchBar_mt_searchIconTint, ContextCompat.getColor(getContext(), R.color.searchBarSearchIconTintColor));
        arrowIconTint = array.getColor(R.styleable.MaterialSearchBar_mt_backIconTint, ContextCompat.getColor(getContext(), R.color.searchBarBackIconTintColor));
        clearIconTint = array.getColor(R.styleable.MaterialSearchBar_mt_clearIconTint, ContextCompat.getColor(getContext(), R.color.searchBarClearIconTintColor));
        navIconTintEnabled = array.getBoolean(R.styleable.MaterialSearchBar_mt_navIconUseTint, true);
        menuIconTintEnabled = array.getBoolean(R.styleable.MaterialSearchBar_mt_menuIconUseTint, true);
        searchIconTintEnabled = array.getBoolean(R.styleable.MaterialSearchBar_mt_searchIconUseTint, true);
        arrowIconTintEnabled = array.getBoolean(R.styleable.MaterialSearchBar_mt_backIconUseTint, true);
        clearIconTintEnabled = array.getBoolean(R.styleable.MaterialSearchBar_mt_clearIconUseTint, true);
        borderlessRippleEnabled = array.getBoolean(R.styleable.MaterialSearchBar_mt_borderlessRippleEnabled, false);

        //Text Related Attributes
        hintText = array.getString(R.styleable.MaterialSearchBar_mt_hint);
        placeholderText = array.getString(R.styleable.MaterialSearchBar_mt_placeholder);
        textColor = array.getColor(R.styleable.MaterialSearchBar_mt_textColor, ContextCompat.getColor(getContext(), R.color.searchBarTextColor));
        hintColor = array.getColor(R.styleable.MaterialSearchBar_mt_hintColor, ContextCompat.getColor(getContext(), R.color.searchBarHintColor));
        placeholderColor = array.getColor(R.styleable.MaterialSearchBar_mt_placeholderColor, ContextCompat.getColor(getContext(), R.color.searchBarPlaceholderColor));
        textCursorColor = array.getColor(R.styleable.MaterialSearchBar_mt_textCursorTint, ContextCompat.getColor(getContext(), R.color.searchBarCursorColor));
        highlightedTextColor = array.getColor(R.styleable.MaterialSearchBar_mt_highlightedTextColor, ContextCompat.getColor(getContext(), R.color.searchBarTextHighlightColor));

        destiny = getResources().getDisplayMetrics().density;
        if (adapter == null) {
            adapter = new DefaultSuggestionsAdapter(LayoutInflater.from(getContext()));
        }
        if (adapter instanceof DefaultSuggestionsAdapter)
            ((DefaultSuggestionsAdapter) adapter).setListener(this);
        adapter.setMaxSuggestionsCount(maxSuggestionCount);
        RecyclerView recyclerView = findViewById(R.id.mt_recycler);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        array.recycle();
        initListener();
        postSetup();

    }

    private void initListener() {
        binding.imageViewClearSearch.setOnClickListener(this);
        setOnClickListener(this);
        binding.imageViewBackArrow.setOnClickListener(this);
        binding.imageViewSearch.setOnClickListener(this);
        binding.editTextSearch.setOnFocusChangeListener(this);
        binding.editTextSearch.setOnEditorActionListener(this);
        binding.imageViewCircleSearch.setOnClickListener(this);
        binding.imageViewSearchBlack.setOnClickListener(this);
    }

    public void inflateMenu(int menuResource) {
        inflateMenuRequest(menuResource, -1);
    }

    public void inflateMenu(int menuResource, int icon) {
        inflateMenuRequest(menuResource, icon);
    }

    private void inflateMenuRequest(int menuResource, int iconResId) {
        int menuResource1 = menuResource;
        if (menuResource1 > 0) {
            if (iconResId != -1) {
                menuIconRes = iconResId;
                binding.imageViewMenu.setImageResource(menuIconRes);
            }
            binding.imageViewMenu.setVisibility(VISIBLE);
            binding.imageViewMenu.setOnClickListener(this);
            popupMenu = new PopupMenu(getContext(), binding.imageViewMenu);
            popupMenu.inflate(menuResource);
            popupMenu.setGravity(Gravity.RIGHT);
        }
    }

    public PopupMenu getMenu() {
        return this.popupMenu;
    }

    private void postSetup() {
        setupTextColors();
        setupRoundedSearchBarEnabled();
        setupSearchBarColor();
        setupIcons();
        setupSearchEditText();
    }

    private void setupRoundedSearchBarEnabled() {
        if (roundedSearchBarEnabled && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            binding.searchbarCardview.setRadius(getResources().getDimension(R.dimen.corner_radius_rounded));
        } else {
            binding.searchbarCardview.setRadius(getResources().getDimension(R.dimen.corner_radius_default));
        }
    }

    private void setupSearchBarColor() {
        binding.searchbarCardview.setCardBackgroundColor(searchBarColor);
        setupDividerColor();
    }

    private void setupDividerColor() {
        binding.viewDivider.setBackgroundColor(dividerColor);
    }

    private void setupTextColors() {
        binding.editTextSearch.setHintTextColor(hintColor);
        binding.editTextSearch.setTextColor(textColor);
        binding.textViewPlaceholder.setTextColor(placeholderColor);
    }

    private void setupSearchEditText() {
        setupCursorColor();
        binding.editTextSearch.setHighlightColor(highlightedTextColor);

        if (hintText != null)
            binding.editTextSearch.setHint(hintText);
        if (placeholderText != null) {
            binding.imageViewBackArrow.setBackground(null);
            binding.textViewPlaceholder.setText(placeholderText);
        }
    }

    private void setupCursorColor() {
        try {
            Field field = TextView.class.getDeclaredField("mEditor");
            field.setAccessible(true);
            Object editor = field.get(binding.editTextSearch);

            field = TextView.class.getDeclaredField("mCursorDrawableRes");
            field.setAccessible(true);
            int cursorDrawableRes = field.getInt(binding.editTextSearch);
            Drawable cursorDrawable = ContextCompat.getDrawable(getContext(), cursorDrawableRes).mutate();
            cursorDrawable.setColorFilter(textCursorColor, PorterDuff.Mode.SRC_IN);
            Drawable[] drawables = {cursorDrawable, cursorDrawable};
            field = editor.getClass().getDeclaredField("mCursorDrawable");
            field.setAccessible(true);
            field.set(editor, drawables);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void setupIcons() {
        navIconResId = R.drawable.ic_menu_animated;
        this.binding.imageViewCircleSearch.setImageResource(navIconResId);
        setNavButtonEnabled(navButtonEnabled);

        //Menu
        if (popupMenu == null) {
            binding.imageViewMenu.setVisibility(GONE);
        }

        //Search
        setSpeechMode(speechMode);

        //Arrow
        this.binding.imageViewBackArrow.setImageResource(arrowIconRes);

        //Clear
        this.binding.imageViewClearSearch.setImageResource(clearIconRes);

        //Colors
        setupNavIconTint();
        setupMenuIconTint();
        setupSearchIconTint();
        setupArrowIconTint();
        setupClearIconTint();
        setupIconRippleStyle();
    }

    private void setupNavIconTint() {
        if (navIconTintEnabled) {
            binding.imageViewCircleSearch.setColorFilter(navIconTint, PorterDuff.Mode.SRC_IN);
        } else {
            binding.imageViewCircleSearch.clearColorFilter();
        }
    }

    private void setupMenuIconTint() {
        if (menuIconTintEnabled) {
            binding.imageViewMenu.setColorFilter(menuIconTint, PorterDuff.Mode.SRC_IN);
        } else {
            binding.imageViewMenu.clearColorFilter();
        }
    }

    private void setupSearchIconTint() {
        if (searchIconTintEnabled) {
            binding.imageViewSearch.setColorFilter(searchIconTint, PorterDuff.Mode.SRC_IN);
        } else {
            binding.imageViewSearch.clearColorFilter();
        }
    }

    private void setupArrowIconTint() {
        if (arrowIconTintEnabled) {
            binding.imageViewBackArrow.setColorFilter(arrowIconTint, PorterDuff.Mode.SRC_IN);
        } else {
            binding.imageViewBackArrow.clearColorFilter();
        }
    }

    private void setupClearIconTint() {
        if (clearIconTintEnabled) {
            binding.imageViewClearSearch.setColorFilter(clearIconTint, PorterDuff.Mode.SRC_IN);
        } else {
            binding.imageViewClearSearch.clearColorFilter();
        }
    }

    private void setupIconRippleStyle() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            TypedValue rippleStyle = new TypedValue();
            if (borderlessRippleEnabled && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackgroundBorderless, rippleStyle, true);
            } else {
                getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, rippleStyle, true);
            }
            binding.imageViewCircleSearch.setBackgroundResource(rippleStyle.resourceId);
            binding.imageViewSearch.setBackgroundResource(rippleStyle.resourceId);
            binding.imageViewMenu.setBackgroundResource(rippleStyle.resourceId);
            binding.imageViewBackArrow.setBackgroundResource(rippleStyle.resourceId);
            binding.imageViewClearSearch.setBackgroundResource(rippleStyle.resourceId);
        } else {
            Log.w(TAG, "setupIconRippleStyle() Only Available On SDK Versions Higher Than 16!");
        }
    }

    public void setOnSearchActionListener(OnSearchActionListener onSearchActionListener) {
        this.onSearchActionListener = onSearchActionListener;
    }

    public void closeSearch() {
        animateNavIcon(false);
        searchOpened = false;
        Animation out = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);
        Animation in = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in_right);
        out.setAnimationListener(this);
        binding.imageViewSearch.setVisibility(VISIBLE);
        binding.searchOpenContainer.startAnimation(out);
        binding.imageViewSearch.startAnimation(in);

        if (placeholderText != null) {
            binding.textViewPlaceholder.setVisibility(VISIBLE);
            binding.textViewPlaceholder.startAnimation(in);
        }
        if (listenerExists())
            onSearchActionListener.onSearchStateChanged(false);
        if (suggestionsVisible) animateSuggestions(getListHeight(false), 0);
    }

    public void openSearch() {
        if (isSearchOpened()) {
            onSearchActionListener.onSearchStateChanged(true);
            binding.editTextSearch.requestFocus();
            return;
        }
        animateNavIcon(true);
        adapter.notifyDataSetChanged();
        searchOpened = true;
        Animation left_in = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in_left);
        Animation left_out = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out_left);
        left_in.setAnimationListener(this);
        binding.textViewPlaceholder.setVisibility(GONE);
        binding.searchOpenContainer.setVisibility(VISIBLE);
        binding.searchOpenContainer.startAnimation(left_in);
        if (listenerExists()) {
            onSearchActionListener.onSearchStateChanged(true);
        }
        binding.imageViewSearch.startAnimation(left_out);
    }

    private void animateNavIcon(boolean menuState) {
        if (menuState) {
            this.binding.imageViewCircleSearch.setImageResource(R.drawable.ic_menu_animated);
        } else {
            this.binding.imageViewCircleSearch.setImageResource(R.drawable.ic_back_animated);
        }
        Drawable mDrawable = binding.imageViewCircleSearch.getDrawable();
        if (mDrawable instanceof Animatable) {
            ((Animatable) mDrawable).start();
        }
    }

    private void animateSuggestions(int from, int to) {
        suggestionsVisible = to > 0;
        final RecyclerView suggestionsList = findViewById(R.id.mt_recycler);
        final ViewGroup.LayoutParams lp = suggestionsList.getLayoutParams();
        if (to == 0 && lp.height == 0)
            return;
        binding.viewDivider.setVisibility(to > 0 ? View.VISIBLE : View.GONE);

        ValueAnimator animator = ValueAnimator.ofInt(from, to);
        animator.setDuration(1200);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                lp.height = (int) animation.getAnimatedValue();
                suggestionsList.setLayoutParams(lp);
            }
        });
        if (adapter.getItemCount() > 0)
            animator.start();
    }

    public void showSuggestionsList() {
        animateSuggestions(0, getListHeight(false));
    }

    public void hideSuggestionsList() {
        animateSuggestions(getListHeight(false), 0);
    }

    public void clearSuggestions() {
        if (suggestionsVisible)
            animateSuggestions(getListHeight(false), 0);
        adapter.clearSuggestions();
    }

    public void showLoading(Boolean showLoading) {
        if (showLoading) {
            binding.imageViewClearSearch.setVisibility(GONE);
            binding.progressbar.setVisibility(VISIBLE);
        } else {
            binding.imageViewClearSearch.setVisibility(VISIBLE);
            binding.progressbar.setVisibility(GONE);

        }
    }

    public boolean isSuggestionsVisible() {
        return suggestionsVisible;
    }

    public boolean isSuggestionsEnabled() {
        return isSuggestionsEnabled;
    }

    public void setSuggestionsEnabled(boolean suggestionsEnabled) {
        isSuggestionsEnabled = suggestionsEnabled;
    }

    public void setMenuIcon(int menuIconResId) {
        this.menuIconRes = menuIconResId;
        this.binding.imageViewMenu.setImageResource(this.menuIconRes);
    }

    public void setSearchIcon(int searchIconResId) {
        this.searchIconRes = searchIconResId;
        this.binding.imageViewSearch.setImageResource(searchIconResId);
    }

    public void setArrowIcon(int arrowIconResId) {
        this.arrowIconRes = arrowIconResId;
        this.binding.imageViewBackArrow.setImageResource(arrowIconRes);
    }

    public void setClearIcon(int clearIconResId) {
        this.clearIconRes = clearIconResId;
        this.binding.imageViewClearSearch.setImageResource(clearIconRes);
    }

    public void setNavIconTint(int navIconTint) {
        this.navIconTint = navIconTint;
        setupNavIconTint();
    }

    public void setMenuIconTint(int menuIconTint) {
        this.menuIconTint = menuIconTint;
        setupMenuIconTint();
    }

    public void setSearchIconTint(int searchIconTint) {
        this.searchIconTint = searchIconTint;
        setupSearchIconTint();
    }

    public void setArrowIconTint(int arrowIconTint) {
        this.arrowIconTint = arrowIconTint;
        setupArrowIconTint();
    }

    public void setClearIconTint(int clearIconTint) {
        this.clearIconTint = clearIconTint;
        setupClearIconTint();
    }

    public void setIconRippleStyle(boolean borderlessRippleEnabled) {
        this.borderlessRippleEnabled = borderlessRippleEnabled;
        setupIconRippleStyle();
    }

    public void setHint(CharSequence hintText) {
        this.hintText = hintText;
        binding.editTextSearch.setHint(hintText);
    }

    public CharSequence getPlaceHolderText() {
        return binding.textViewPlaceholder.getText();
    }

    public void setSpeechMode(boolean speechMode) {
        this.speechMode = speechMode;
        if (speechMode) {
            binding.imageViewSearch.setImageResource(speechIconRes);
            binding.imageViewSearch.setClickable(true);
        } else {
            binding.imageViewSearch.setImageResource(searchIconRes);
            binding.imageViewSearch.setClickable(false);
        }
    }

    public boolean isSpeechModeEnabled() {
        return speechMode;
    }

    public boolean isSearchOpened() {
        return searchOpened;
    }

    public void setMaxSuggestionCount(int maxSuggestionsCount) {
        this.maxSuggestionCount = maxSuggestionsCount;
        adapter.setMaxSuggestionsCount(maxSuggestionsCount);
    }

    public void setCustomSuggestionAdapter(SuggestionsAdapter suggestionAdapter) {
        this.adapter = suggestionAdapter;
        RecyclerView recyclerView = findViewById(R.id.mt_recycler);
        recyclerView.setAdapter(adapter);
    }

    public List getLastSuggestions() {
        return adapter.getSuggestions();
    }


//    public void setLastSuggestions(List suggestions) {
//        adapter.setSuggestions(suggestions);
//    }

    public void setLastSuggestions(List suggestions, List descriptions, List urls) {
        adapter.setSuggestions(suggestions);
        adapter.setdescriptions(descriptions);
        adapter.setUrls(urls);
    }

    public void setLastSuggestions( ModelDto models) {
        adapter.setSuggestions(models.getTitle());
        adapter.setdescriptions(models.getDescription());
        adapter.setUrls(models.getUrls());
    }

    public void updateLastSuggestions(List suggestions) {
        int startHeight = getListHeight(false);
        if (suggestions.size() > 0) {
            List newSuggestions = new ArrayList<>(suggestions);
            adapter.setSuggestions(newSuggestions);
            animateSuggestions(startHeight, getListHeight(false));
        } else {
            animateSuggestions(startHeight, 0);
        }
    }

    public void setSuggestionsClickListener(SuggestionsAdapter.OnItemViewClickListener listener) {
        if (adapter instanceof DefaultSuggestionsAdapter)
            ((DefaultSuggestionsAdapter) adapter).setListener(listener);
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        setupTextColors();
    }

    public void setTextHintColor(int hintColor) {
        this.hintColor = hintColor;
        setupTextColors();
    }

    public void setPlaceHolderColor(int placeholderColor) {
        this.placeholderColor = placeholderColor;
        setupTextColors();
    }

    public void setTextHighlightColor(int highlightedTextColor) {
        this.highlightedTextColor = highlightedTextColor;
        binding.editTextSearch.setHighlightColor(highlightedTextColor);
    }

    public void setDividerColor(int dividerColor) {
        this.dividerColor = dividerColor;
        setupDividerColor();
    }

    public void setNavButtonEnabled(boolean navButtonEnabled) {
        this.navButtonEnabled = navButtonEnabled;
        if (navButtonEnabled) {
            binding.imageViewCircleSearch.setVisibility(VISIBLE);
            binding.imageViewCircleSearch.setClickable(true);
            binding.imageViewBackArrow.setVisibility(GONE);
        } else {
            binding.imageViewCircleSearch.setVisibility(GONE);
            binding.imageViewCircleSearch.setClickable(false);
            binding.imageViewBackArrow.setVisibility(VISIBLE);
        }
        binding.imageViewCircleSearch.requestLayout();
        binding.textViewPlaceholder.requestLayout();
        binding.imageViewBackArrow.requestLayout();
    }

    public void setRoundedSearchBarEnabled(boolean roundedSearchBarEnabled) {
        this.roundedSearchBarEnabled = roundedSearchBarEnabled;
        setupRoundedSearchBarEnabled();
    }

    public void setCardViewElevation(int elevation) {
        binding.searchbarCardview.setCardElevation(elevation);
    }

    public String getText() {
        return binding.editTextSearch.getText().toString();
    }

    public void setText(String text) {
        binding.editTextSearch.setText(text);
    }

    public void addTextChangeListener(TextWatcher textWatcher) {
        binding.editTextSearch.addTextChangedListener(textWatcher);
    }

    public EditText getSearchEditText() {
        return binding.editTextSearch;
    }

    public TextView getPlaceHolderView() {
        return binding.textViewPlaceholder;
    }

    public void setPlaceHolder(CharSequence placeholder) {
        this.placeholderText = placeholder;
        binding.textViewPlaceholder.setText(placeholder);
    }

    private boolean listenerExists() {
        return onSearchActionListener != null;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == getId()) {
            if (!searchOpened) {
                openSearch();
            }
        } else if (id == R.id.imageView_back_arrow) {
            closeSearch();
        } else if (id == R.id.imageView_search) {
            if (listenerExists())
                onSearchActionListener.onButtonClicked(BUTTON_SPEECH);
        } else if (id == R.id.imageView_clear_search) {
            binding.editTextSearch.setText("");
        } else if (id == R.id.imageView_menu) {
            popupMenu.show();
        } else if (id == R.id.imageView_circle_search) {
            int button = searchOpened ? BUTTON_BACK : BUTTON_NAVIGATION;
            if (searchOpened) {
                closeSearch();
            }
            if (listenerExists()) {
                onSearchActionListener.onButtonClicked(button);
            }
        }
    }

    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (!searchOpened) {
            binding.searchOpenContainer.setVisibility(GONE);
            // searchEdit.setText("");
        } else {
            binding.imageViewSearch.setVisibility(GONE);
            binding.editTextSearch.requestFocus();
            if (!suggestionsVisible && isSuggestionsEnabled)
                showSuggestionsList();
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (hasFocus) {
            imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
        } else {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (listenerExists())
            onSearchActionListener.onSearchConfirmed(binding.editTextSearch.getText());
        if (suggestionsVisible)
            hideSuggestionsList();
        if (adapter instanceof DefaultSuggestionsAdapter)
            adapter.addSuggestion(binding.editTextSearch.getText().toString());
        return true;
    }

    private int getListHeight(boolean isSubtraction) {
        if (!isSubtraction)
            return (int) (adapter.getListHeight() * destiny);
        return (int) (((adapter.getItemCount() - 1) * adapter.getSingleViewHeight()) * destiny);
    }

    @Override
    public void OnItemClickListener(int position, View v) {
        if (v.getTag() instanceof String) {
            binding.editTextSearch.setText((String) v.getTag());
        }
    }

    @Override
    public void OnItemDeleteListener(int position, View v) {
        if (v.getTag() instanceof String) {
            /*Order of two line should't be change,
            because should calculate the height of item first*/
            animateSuggestions(getListHeight(false), getListHeight(true));
            adapter.deleteSuggestion(position, v.getTag());
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.isSearchBarVisible = searchOpened ? VIEW_VISIBLE : VIEW_INVISIBLE;
        savedState.suggestionsVisible = suggestionsVisible ? VIEW_VISIBLE : VIEW_INVISIBLE;
        savedState.speechMode = speechMode ? VIEW_VISIBLE : VIEW_INVISIBLE;
        savedState.navIconResId = navIconResId;
        savedState.searchIconRes = searchIconRes;
        savedState.suggestions = getLastSuggestions();
        savedState.maxSuggestions = maxSuggestionCount;
        if (hintText != null) savedState.hint = hintText.toString();
        return savedState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        searchOpened = savedState.isSearchBarVisible == VIEW_VISIBLE;
        suggestionsVisible = savedState.suggestionsVisible == VIEW_VISIBLE;
        //setLastSuggestions(savedState.suggestions);
        if (suggestionsVisible)
            animateSuggestions(0, getListHeight(false));
        if (searchOpened) {
            binding.searchOpenContainer.setVisibility(VISIBLE);
            binding.textViewPlaceholder.setVisibility(GONE);
            binding.imageViewSearch.setVisibility(GONE);
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && searchOpened) {
            animateSuggestions(getListHeight(false), 0);
            closeSearch();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    public interface OnSearchActionListener {
        void onSearchStateChanged(boolean enabled);

        void onSearchConfirmed(CharSequence text);

        void onButtonClicked(int buttonCode);
    }

    private static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel source) {
                return new SavedState(source);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        private int isSearchBarVisible;
        private int suggestionsVisible;
        private int speechMode;
        private int searchIconRes;
        private int navIconResId;
        private String hint;
        private List suggestions;
        private int maxSuggestions;

        public SavedState(Parcel source) {
            super(source);
            isSearchBarVisible = source.readInt();
            suggestionsVisible = source.readInt();
            speechMode = source.readInt();
            navIconResId = source.readInt();
            searchIconRes = source.readInt();
            hint = source.readString();
            suggestions = source.readArrayList(null);
            maxSuggestions = source.readInt();
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(isSearchBarVisible);
            out.writeInt(suggestionsVisible);
            out.writeInt(speechMode);

            out.writeInt(searchIconRes);
            out.writeInt(navIconResId);
            out.writeString(hint);
            out.writeList(suggestions);
            out.writeInt(maxSuggestions);
        }
    }
}

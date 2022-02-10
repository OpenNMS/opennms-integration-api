var style$5 = "";
const getSafeId = function(str) {
  str = str || "feather";
  const random = Math.floor(Math.random() * 1e9);
  return [str.replace(/\s+/g, "-"), Date.now(), random].join("-");
};
var style$4 = "";
var style$3 = "";
var __defProp$4 = Object.defineProperty;
var __getOwnPropSymbols$4 = Object.getOwnPropertySymbols;
var __hasOwnProp$4 = Object.prototype.hasOwnProperty;
var __propIsEnum$4 = Object.prototype.propertyIsEnumerable;
var __defNormalProp$4 = (obj, key, value) => key in obj ? __defProp$4(obj, key, { enumerable: true, configurable: true, writable: true, value }) : obj[key] = value;
var __spreadValues$4 = (a, b) => {
  for (var prop in b || (b = {}))
    if (__hasOwnProp$4.call(b, prop))
      __defNormalProp$4(a, prop, b[prop]);
  if (__getOwnPropSymbols$4)
    for (var prop of __getOwnPropSymbols$4(b)) {
      if (__propIsEnum$4.call(b, prop))
        __defNormalProp$4(a, prop, b[prop]);
    }
  return a;
};
const toRaw = window["Vue"].toRaw;
const h$2 = window["Vue"].h;
var _export_sfc$7 = (sfc, props) => {
  const target = sfc.__vccOpts || sfc;
  for (const [key, val] of props) {
    target[key] = val;
  }
  return target;
};
const _sfc_main$8 = {
  props: {
    icon: {
      type: Object,
      required: false
    },
    flex: {
      type: Boolean,
      default: false
    },
    title: {
      type: String,
      required: false
    }
  },
  render() {
    const attrs = this.$attrs;
    const cls = attrs["class"] ? attrs["class"].split(" ").reduce((o, key) => {
      o[key] = true;
      return o;
    }, {}) : {};
    const _attrs = {};
    cls["feather-icon"] = true;
    if (this.flex) {
      cls["feather-icon-flex"] = true;
    }
    if (this.title) {
      _attrs["aria-label"] = this.title;
      _attrs["aria-hidden"] = "false";
    } else {
      _attrs["aria-hidden"] = "true";
    }
    _attrs["focusable"] = "false";
    _attrs["role"] = "img";
    let icon = toRaw(this.icon);
    if (this.$slots.default) {
      return h$2("span", { class: "feather-icon-container" }, [
        h$2(this.$slots.default()[0], __spreadValues$4({
          class: cls
        }, _attrs))
      ]);
    }
    return h$2(icon, __spreadValues$4({
      class: cls
    }, _attrs));
  }
};
var FeatherIcon = /* @__PURE__ */ _export_sfc$7(_sfc_main$8, [["__scopeId", "data-v-0aa06a12"]]);
const openBlock$3 = window["Vue"].openBlock;
const createElementBlock$3 = window["Vue"].createElementBlock;
const createElementVNode$3 = window["Vue"].createElementVNode;
var _export_sfc$6 = (sfc, props) => {
  for (const [key, val] of props) {
    sfc[key] = val;
  }
  return sfc;
};
const _sfc_main$7 = {};
const _hoisted_1$5 = {
  xmlns: "http://www.w3.org/2000/svg",
  viewBox: "0 0 24 24"
};
const _hoisted_2$3 = /* @__PURE__ */ createElementVNode$3("path", { d: "M19,17.55,13.43,12,19,6.4A1,1,0,1,0,17.58,5L12,10.58,6.42,5A1,1,0,0,0,5,6.42L10.6,12,5,17.6A1,1,0,0,0,5,19a1,1,0,0,0,.71.29,1,1,0,0,0,.71-.3L12,13.41,17.6,19a1,1,0,0,0,.71.29A1,1,0,0,0,19,19,1,1,0,0,0,19,17.55Z" }, null, -1);
const _hoisted_3$3 = [
  _hoisted_2$3
];
function _sfc_render$6(_ctx, _cache) {
  return openBlock$3(), createElementBlock$3("svg", _hoisted_1$5, _hoisted_3$3);
}
var Cancel = /* @__PURE__ */ _export_sfc$6(_sfc_main$7, [["render", _sfc_render$6]]);
const openBlock$2 = window["Vue"].openBlock;
const createElementBlock$2 = window["Vue"].createElementBlock;
const createElementVNode$2 = window["Vue"].createElementVNode;
var _export_sfc$5 = (sfc, props) => {
  for (const [key, val] of props) {
    sfc[key] = val;
  }
  return sfc;
};
const _sfc_main$6 = {};
const _hoisted_1$4 = {
  xmlns: "http://www.w3.org/2000/svg",
  viewBox: "0 0 24 24"
};
const _hoisted_2$2 = /* @__PURE__ */ createElementVNode$2("path", { d: "M12,2A10,10,0,1,0,22,12,10,10,0,0,0,12,2Zm0,18a8,8,0,1,1,8-8A8,8,0,0,1,12,20Z" }, null, -1);
const _hoisted_3$2 = /* @__PURE__ */ createElementVNode$2("rect", {
  x: "11",
  y: "7",
  width: "2",
  height: "7",
  rx: "1"
}, null, -1);
const _hoisted_4$1 = /* @__PURE__ */ createElementVNode$2("rect", {
  x: "11",
  y: "15",
  width: "2",
  height: "2",
  rx: "0.65"
}, null, -1);
const _hoisted_5 = [
  _hoisted_2$2,
  _hoisted_3$2,
  _hoisted_4$1
];
function _sfc_render$5(_ctx, _cache) {
  return openBlock$2(), createElementBlock$2("svg", _hoisted_1$4, _hoisted_5);
}
var Warning = /* @__PURE__ */ _export_sfc$5(_sfc_main$6, [["render", _sfc_render$5]]);
var __defProp$3 = Object.defineProperty;
var __defProps$3 = Object.defineProperties;
var __getOwnPropDescs$3 = Object.getOwnPropertyDescriptors;
var __getOwnPropSymbols$3 = Object.getOwnPropertySymbols;
var __hasOwnProp$3 = Object.prototype.hasOwnProperty;
var __propIsEnum$3 = Object.prototype.propertyIsEnumerable;
var __defNormalProp$3 = (obj, key, value) => key in obj ? __defProp$3(obj, key, { enumerable: true, configurable: true, writable: true, value }) : obj[key] = value;
var __spreadValues$3 = (a, b) => {
  for (var prop in b || (b = {}))
    if (__hasOwnProp$3.call(b, prop))
      __defNormalProp$3(a, prop, b[prop]);
  if (__getOwnPropSymbols$3)
    for (var prop of __getOwnPropSymbols$3(b)) {
      if (__propIsEnum$3.call(b, prop))
        __defNormalProp$3(a, prop, b[prop]);
    }
  return a;
};
var __spreadProps$3 = (a, b) => __defProps$3(a, __getOwnPropDescs$3(b));
const resolveComponent$1 = window["Vue"].resolveComponent;
const openBlock$1 = window["Vue"].openBlock;
const createElementBlock$1 = window["Vue"].createElementBlock;
const createVNode$1 = window["Vue"].createVNode;
const createBlock = window["Vue"].createBlock;
const withModifiers = window["Vue"].withModifiers;
const markRaw = window["Vue"].markRaw;
const inject$1 = window["Vue"].inject;
const computed$1 = window["Vue"].computed;
const normalizeClass$1 = window["Vue"].normalizeClass;
const createElementVNode$1 = window["Vue"].createElementVNode;
const toDisplayString$1 = window["Vue"].toDisplayString;
const withDirectives = window["Vue"].withDirectives;
const normalizeStyle = window["Vue"].normalizeStyle;
const vShow = window["Vue"].vShow;
const renderSlot$1 = window["Vue"].renderSlot;
const createCommentVNode$1 = window["Vue"].createCommentVNode;
var _export_sfc$4 = (sfc, props) => {
  const target = sfc.__vccOpts || sfc;
  for (const [key, val] of props) {
    target[key] = val;
  }
  return target;
};
const _sfc_main$4 = {
  props: {
    title: {
      type: String,
      default: ""
    },
    icon: {
      type: Object,
      required: true
    }
  },
  components: {
    FeatherIcon
  }
};
const _hoisted_1$2 = ["title"];
function _sfc_render$4(_ctx, _cache, $props, $setup, $data, $options) {
  const _component_FeatherIcon = resolveComponent$1("FeatherIcon");
  return openBlock$1(), createElementBlock$1("a", {
    title: $props.title,
    class: "action-icon hide-when-disabled",
    href: "#"
  }, [
    createVNode$1(_component_FeatherIcon, { icon: $props.icon }, null, 8, ["icon"])
  ], 8, _hoisted_1$2);
}
var ActionIcon = /* @__PURE__ */ _export_sfc$4(_sfc_main$4, [["render", _sfc_render$4], ["__scopeId", "data-v-d1dfbdf8"]]);
const _sfc_main$3$1 = {
  emits: ["clear"],
  props: {
    clear: {
      type: String,
      default: ""
    }
  },
  computed: {
    clearIcon() {
      return Cancel;
    }
  },
  components: {
    ActionIcon
  }
};
function _sfc_render$3(_ctx, _cache, $props, $setup, $data, $options) {
  const _component_ActionIcon = resolveComponent$1("ActionIcon");
  return openBlock$1(), createBlock(_component_ActionIcon, {
    onClick: _cache[0] || (_cache[0] = withModifiers(($event) => _ctx.$emit("clear"), ["stop", "prevent"])),
    title: $props.clear,
    icon: $options.clearIcon,
    "data-ref-id": "feather-form-element-clear"
  }, null, 8, ["title", "icon"]);
}
var ClearIcon = /* @__PURE__ */ _export_sfc$4(_sfc_main$3$1, [["render", _sfc_render$3]]);
const _sfc_main$2$1 = {
  computed: {
    errorIcon() {
      return markRaw(Warning);
    }
  },
  components: {
    FeatherIcon
  }
};
function _sfc_render$2(_ctx, _cache, $props, $setup, $data, $options) {
  const _component_FeatherIcon = resolveComponent$1("FeatherIcon");
  return openBlock$1(), createBlock(_component_FeatherIcon, {
    icon: $options.errorIcon,
    class: "error-icon hide-when-disabled"
  }, null, 8, ["icon"]);
}
var ErrorIcon = /* @__PURE__ */ _export_sfc$4(_sfc_main$2$1, [["render", _sfc_render$2], ["__scopeId", "data-v-41337cd6"]]);
const _sfc_main$1$2 = {
  emits: ["clear", "wrapper-click"],
  props: {
    for: {
      type: String,
      required: true
    },
    focused: {
      type: Boolean,
      default: false
    },
    showClear: {
      type: Boolean,
      default: false
    },
    clearText: {
      type: String
    },
    raised: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      prefixWidth: 0,
      prefixObserver: null
    };
  },
  setup() {
    const options = inject$1("wrapperOptions");
    const errorMessage = inject$1("validationErrorMessage", false);
    const error = computed$1(() => {
      if (options.error) {
        return options.error;
      }
      if (errorMessage && errorMessage.value) {
        return errorMessage.value;
      }
      return false;
    });
    return __spreadProps$3(__spreadValues$3({}, options), { error });
  },
  computed: {
    computedClearText() {
      if (this.clearText) {
        return this.clearText;
      }
      if (this.clear) {
        return this.clear;
      }
      return "";
    },
    inputId() {
      return this.for;
    },
    hasPre() {
      const hasPre = this.$slots.pre && this.$slots.pre().findIndex((o) => o.children && o.children.length !== 0) !== -1;
      return hasPre;
    },
    containerCls() {
      const cls = [];
      if (this.raised) {
        cls.push("raised");
      }
      if (this.focused) {
        cls.push("focused");
      }
      if (this.error) {
        cls.push("error");
      }
      if (this.background) {
        cls.push("background");
      }
      if (this.disabled) {
        cls.push("disabled");
      }
      if (this.inline) {
        cls.push("inline");
      }
      return cls;
    },
    labelLeft() {
      if (this.raised) {
        return 12;
      } else {
        return this.prefixWidth > 0 ? this.prefixWidth + 16 + 4 : 16;
      }
    }
  },
  methods: {
    handleWrapperClick(e) {
      if (!this.disabled) {
        this.$emit("wrapper-click", e);
      }
    }
  },
  mounted() {
    const element = this.$el.querySelector(".prefix");
    if (element) {
      const config = { childList: true, subtree: true };
      const callback = () => {
        const prefix = this.$el.querySelector(".prefix");
        this.prefixWidth = prefix ? prefix.offsetWidth : 0;
      };
      this.prefixObserver = new MutationObserver(callback);
      this.prefixObserver.observe(element, config);
      callback();
    }
  },
  unmounted() {
    if (this.prefixObserver) {
      this.prefixObserver.disconnect();
    }
  },
  components: {
    ClearIcon,
    ErrorIcon
  }
};
const _hoisted_1$1$1 = {
  "aria-hidden": "true",
  class: "feather-input-border"
};
const _hoisted_2$1$1 = ["for"];
const _hoisted_3$1 = { class: "prefix" };
const _hoisted_4 = { class: "post" };
function _sfc_render$1$1(_ctx, _cache, $props, $setup, $data, $options) {
  const _component_ClearIcon = resolveComponent$1("ClearIcon");
  const _component_ErrorIcon = resolveComponent$1("ErrorIcon");
  return openBlock$1(), createElementBlock$1("div", {
    class: normalizeClass$1(["feather-input-wrapper-container", $options.containerCls])
  }, [
    createElementVNode$1("fieldset", _hoisted_1$1$1, [
      createElementVNode$1("legend", null, toDisplayString$1(_ctx.label), 1)
    ]),
    withDirectives(createElementVNode$1("label", {
      class: "feather-input-label",
      for: $options.inputId,
      style: normalizeStyle({ left: $options.labelLeft + "px" }),
      "data-ref-id": "feather-form-element-label"
    }, toDisplayString$1(_ctx.label), 13, _hoisted_2$1$1), [
      [vShow, !_ctx.hideLabel]
    ]),
    createElementVNode$1("div", {
      class: normalizeClass$1(["feather-input-wrapper", { "has-prefix": $options.hasPre }]),
      onClick: _cache[1] || (_cache[1] = (...args) => $options.handleWrapperClick && $options.handleWrapperClick(...args))
    }, [
      createElementVNode$1("div", _hoisted_3$1, [
        renderSlot$1(_ctx.$slots, "pre", {}, void 0, true)
      ]),
      renderSlot$1(_ctx.$slots, "default", {}, void 0, true),
      createElementVNode$1("div", _hoisted_4, [
        $props.showClear && $options.computedClearText ? (openBlock$1(), createBlock(_component_ClearIcon, {
          key: 0,
          clear: $options.computedClearText,
          onClear: _cache[0] || (_cache[0] = ($event) => _ctx.$emit("clear"))
        }, null, 8, ["clear"])) : createCommentVNode$1("", true),
        $setup.error ? (openBlock$1(), createBlock(_component_ErrorIcon, { key: 1 })) : createCommentVNode$1("", true),
        renderSlot$1(_ctx.$slots, "post", {}, void 0, true)
      ])
    ], 2)
  ], 2);
}
var InputWrapper = /* @__PURE__ */ _export_sfc$4(_sfc_main$1$2, [["render", _sfc_render$1$1], ["__scopeId", "data-v-3828e91d"]]);
var InputWrapperMixin = {
  props: {
    label: {
      type: String,
      required: true
    },
    error: {
      type: String
    },
    clear: {
      type: String,
      default: ""
    },
    background: {
      type: Boolean,
      default: false
    },
    disabled: {
      type: Boolean,
      default: false
    },
    inline: {
      type: Boolean,
      default: false
    },
    hideLabel: {
      type: Boolean,
      default: false
    }
  },
  provide() {
    return {
      wrapperOptions: this.$props
    };
  }
};
const _sfc_main$5 = {
  computed: {
    hasContent() {
      const hasRightSlot = this.$slots.right && this.$slots.right().findIndex((o) => o.children && o.children.length !== 0) !== -1;
      return !!this.error || !!this.hint || hasRightSlot;
    }
  },
  props: {
    id: {
      type: String,
      required: true
    }
  },
  setup() {
    const options = inject$1("subTextOptions");
    const errorMessage = inject$1("validationErrorMessage", false);
    const error = computed$1(() => {
      if (options.error) {
        return options.error;
      }
      if (errorMessage && errorMessage.value) {
        return errorMessage.value;
      }
      return "";
    });
    return __spreadProps$3(__spreadValues$3({}, options), { error });
  }
};
const _hoisted_1$3 = ["id"];
const _hoisted_2 = {
  key: 0,
  class: "feather-input-hint",
  "data-ref-id": "feather-form-element-hint"
};
const _hoisted_3 = {
  key: 1,
  class: "feather-input-error",
  "data-ref-id": "feather-form-element-error",
  "aria-live": "assertive"
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return withDirectives((openBlock$1(), createElementBlock$1("div", {
    class: "feather-input-sub-text",
    id: $props.id
  }, [
    _ctx.hint && !$setup.error.length ? (openBlock$1(), createElementBlock$1("div", _hoisted_2, toDisplayString$1(_ctx.hint), 1)) : createCommentVNode$1("", true),
    $setup.error.length > 0 ? (openBlock$1(), createElementBlock$1("div", _hoisted_3, toDisplayString$1($setup.error), 1)) : createCommentVNode$1("", true),
    renderSlot$1(_ctx.$slots, "right", {}, void 0, true)
  ], 8, _hoisted_1$3)), [
    [vShow, $options.hasContent]
  ]);
}
var InputSubText = /* @__PURE__ */ _export_sfc$4(_sfc_main$5, [["render", _sfc_render], ["__scopeId", "data-v-51d97390"]]);
var InputSubTextMixin = {
  props: {
    hint: {
      type: String
    },
    error: {
      type: String
    }
  },
  provide() {
    return {
      subTextOptions: this.$props
    };
  }
};
var InputInheritAttrsMixin = {
  inheritAttrs: false,
  computed: {
    inherittedAttrs() {
      return {
        class: this.$attrs.class,
        "data-ref-id": this.$attrs["data-ref-id"]
      };
    }
  }
};
var __defProp$2 = Object.defineProperty;
var __defProps$2 = Object.defineProperties;
var __getOwnPropDescs$2 = Object.getOwnPropertyDescriptors;
var __getOwnPropSymbols$2 = Object.getOwnPropertySymbols;
var __hasOwnProp$2 = Object.prototype.hasOwnProperty;
var __propIsEnum$2 = Object.prototype.propertyIsEnumerable;
var __defNormalProp$2 = (obj, key, value) => key in obj ? __defProp$2(obj, key, { enumerable: true, configurable: true, writable: true, value }) : obj[key] = value;
var __spreadValues$2 = (a, b) => {
  for (var prop in b || (b = {}))
    if (__hasOwnProp$2.call(b, prop))
      __defNormalProp$2(a, prop, b[prop]);
  if (__getOwnPropSymbols$2)
    for (var prop of __getOwnPropSymbols$2(b)) {
      if (__propIsEnum$2.call(b, prop))
        __defNormalProp$2(a, prop, b[prop]);
    }
  return a;
};
var __spreadProps$2 = (a, b) => __defProps$2(a, __getOwnPropDescs$2(b));
const inject = window["Vue"].inject;
const ref = window["Vue"].ref;
const provide = window["Vue"].provide;
const watch = window["Vue"].watch;
const toRef = window["Vue"].toRef;
const computed = window["Vue"].computed;
const resolveComponent = window["Vue"].resolveComponent;
const openBlock = window["Vue"].openBlock;
const createElementBlock = window["Vue"].createElementBlock;
const mergeProps = window["Vue"].mergeProps;
const createVNode = window["Vue"].createVNode;
const normalizeClass = window["Vue"].normalizeClass;
const withCtx = window["Vue"].withCtx;
const renderSlot = window["Vue"].renderSlot;
const createElementVNode = window["Vue"].createElementVNode;
const toHandlers = window["Vue"].toHandlers;
const toDisplayString = window["Vue"].toDisplayString;
const createCommentVNode = window["Vue"].createCommentVNode;
window["Vue"].nextTick;
window["Vue"].Fragment;
window["Vue"].renderList;
window["Vue"].withModifiers;
const useValidation = (inputId, value, label, schema) => {
  const form = inject("featherForm", false);
  if (schema && form && inputId.value) {
    const errorMessage = ref("");
    provide("validationErrorMessage", errorMessage);
    const validate = () => {
      try {
        schema.validateSync(value.value);
        errorMessage.value = "";
        return { success: true };
      } catch (e) {
        errorMessage.value = e.errors[0];
        return {
          success: false,
          message: e.errors[0],
          inputId: inputId.value,
          label
        };
      }
    };
    form.register(inputId.value, validate);
    watch(inputId, (curr, old) => {
      if (curr) {
        form.reregister(old, curr);
      } else {
        form.deregister(old);
      }
    });
    return { validate };
  }
  return { validate: () => true };
};
var _export_sfc$3 = (sfc, props) => {
  const target = sfc.__vccOpts || sfc;
  for (const [key, val] of props) {
    target[key] = val;
  }
  return target;
};
const _sfc_main$1$1 = {
  model: {
    prop: "modelValue",
    event: "update:modelValue"
  },
  mixins: [InputWrapperMixin, InputSubTextMixin, InputInheritAttrsMixin],
  emits: ["update:modelValue"],
  props: {
    type: {
      type: String,
      default: "text"
    },
    modelValue: {
      type: [String, Number]
    },
    maxlength: {
      type: Number,
      required: false,
      default: 0
    },
    schema: {
      type: Object,
      required: false
    },
    id: {
      type: String,
      required: false
    }
  },
  setup(props) {
    const incomingId = toRef(props, "id");
    const inputId = computed(() => {
      if (incomingId.value) {
        return incomingId.value;
      }
      return getSafeId("feather-input-label");
    });
    const internalValue = ref();
    const { validate } = useValidation(inputId, internalValue, props.label, props.schema);
    return { inputId, internalValue, validate };
  },
  data() {
    return {
      focused: false
    };
  },
  computed: {
    descriptionId() {
      return getSafeId("feather-input-description");
    },
    showClear() {
      return !!(this.internalValue && this.internalValue.length > 0);
    },
    contentCls() {
      const cls = [];
      if (this.error) {
        cls.push("error");
      }
      if (this.disabled) {
        cls.push("disabled");
      }
      if (this.focused) {
        cls.push("focused");
      }
      return cls;
    },
    isRaised() {
      if (this.type === "number") {
        if (this.internalValue !== void 0 && this.internalValue !== null || this.focused) {
          return true;
        }
        return false;
      }
      if (this.internalValue || this.focused) {
        return true;
      }
      return false;
    },
    attrs() {
      const attrs = __spreadValues$2({}, this.$attrs);
      delete attrs.placeholder;
      if (!attrs["aria-invalid"]) {
        attrs["aria-invalid"] = !!this.error;
      }
      return __spreadProps$2(__spreadValues$2(__spreadValues$2({}, attrs), this.listeners), {
        class: "",
        type: this.type,
        id: this.inputId,
        name: this.inputId,
        disabled: this.disabled,
        "aria-disabled": this.disabled,
        "aria-describedby": (attrs["aria-describedby"] || "").split(" ").concat([this.descriptionId]).filter((x) => x.length > 0).join(" "),
        value: this.internalValue
      });
    },
    listeners() {
      return {
        onFocus: (e) => {
          this.handleFocus();
          if (this.$attrs.onFocus) {
            this.$attrs.onFocus(e);
          }
        },
        onBlur: (e) => {
          this.validate();
          this.handleBlur();
          if (this.$attrs.onBlur) {
            this.$attrs.onBlur(e);
          }
        },
        onInput: (e) => {
          this.handleInput(e);
        }
      };
    },
    charCount() {
      return `${this.internalValue && this.internalValue.length || "0"} / ${this.maxlength}`;
    }
  },
  watch: {
    modelValue: {
      immediate: true,
      handler(v) {
        this.internalValue = v;
      }
    },
    internalValue: {
      immediate: true,
      handler(v) {
        this.$emit("update:modelValue", v);
      }
    }
  },
  methods: {
    handleClear() {
      this.internalValue = "";
      this.focus();
    },
    handleWrapperClick() {
      this.$refs.input.focus();
    },
    handleFocus() {
      this.focused = true;
    },
    handleBlur() {
      this.focused = false;
    },
    handleInput(e) {
      this.internalValue = e.target.value;
      this.$emit("update:modelValue", this.internalValue);
    },
    focus() {
      this.$nextTick(() => {
        this.$refs.input.focus();
      });
    }
  },
  components: {
    InputSubText,
    InputWrapper
  }
};
const _hoisted_1$1 = ["maxlength"];
const _hoisted_2$1 = {
  key: 0,
  class: "feather-input-count",
  "data-ref-id": "feather-form-element-count"
};
function _sfc_render$1(_ctx, _cache, $props, $setup, $data, $options) {
  const _component_InputWrapper = resolveComponent("InputWrapper");
  const _component_InputSubText = resolveComponent("InputSubText");
  return openBlock(), createElementBlock("div", mergeProps(_ctx.inherittedAttrs, { class: "feather-input-container" }), [
    createVNode(_component_InputWrapper, {
      for: $setup.inputId,
      raised: $options.isRaised,
      focused: $data.focused,
      "show-clear": $options.showClear,
      onWrapperClick: $options.handleWrapperClick,
      onClear: $options.handleClear,
      class: normalizeClass(["feather-input-content", $options.contentCls])
    }, {
      pre: withCtx(() => [
        renderSlot(_ctx.$slots, "pre", {}, void 0, true)
      ]),
      post: withCtx(() => [
        renderSlot(_ctx.$slots, "post", {}, void 0, true)
      ]),
      default: withCtx(() => [
        createElementVNode("input", mergeProps($options.attrs, {
          class: "feather-input",
          ref: "input"
        }, toHandlers($options.listeners), {
          "data-ref-id": "feather-input",
          maxlength: $props.maxlength > 0 ? $props.maxlength : false
        }), null, 16, _hoisted_1$1)
      ]),
      _: 3
    }, 8, ["for", "raised", "focused", "show-clear", "onWrapperClick", "onClear", "class"]),
    createVNode(_component_InputSubText, { id: $options.descriptionId }, {
      right: withCtx(() => [
        $props.maxlength ? (openBlock(), createElementBlock("div", _hoisted_2$1, toDisplayString($options.charCount), 1)) : createCommentVNode("", true)
      ]),
      _: 1
    }, 8, ["id"])
  ], 16);
}
var FeatherInput = /* @__PURE__ */ _export_sfc$3(_sfc_main$1$1, [["render", _sfc_render$1], ["__scopeId", "data-v-756d6a1a"]]);
var style$2 = "";
var style$1 = "";
var __defProp$1 = Object.defineProperty;
var __defProps$1 = Object.defineProperties;
var __getOwnPropDescs$1 = Object.getOwnPropertyDescriptors;
var __getOwnPropSymbols$1 = Object.getOwnPropertySymbols;
var __hasOwnProp$1 = Object.prototype.hasOwnProperty;
var __propIsEnum$1 = Object.prototype.propertyIsEnumerable;
var __defNormalProp$1 = (obj, key, value) => key in obj ? __defProp$1(obj, key, { enumerable: true, configurable: true, writable: true, value }) : obj[key] = value;
var __spreadValues$1 = (a, b) => {
  for (var prop in b || (b = {}))
    if (__hasOwnProp$1.call(b, prop))
      __defNormalProp$1(a, prop, b[prop]);
  if (__getOwnPropSymbols$1)
    for (var prop of __getOwnPropSymbols$1(b)) {
      if (__propIsEnum$1.call(b, prop))
        __defNormalProp$1(a, prop, b[prop]);
    }
  return a;
};
var __spreadProps$1 = (a, b) => __defProps$1(a, __getOwnPropDescs$1(b));
const h$1 = window["Vue"].h;
var _export_sfc$2 = (sfc, props) => {
  const target = sfc.__vccOpts || sfc;
  for (const [key, val] of props) {
    target[key] = val;
  }
  return target;
};
const _sfc_main$3 = {
  props: {
    center: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      pressed: false,
      active: false,
      styles: {},
      failsafe: 0
    };
  },
  computed: {
    parent() {
      return this.$el.parentNode;
    }
  },
  methods: {
    onClick($event) {
      this.pressed = false;
      this.active = false;
      requestAnimationFrame(() => {
        const { clientWidth, clientHeight } = this.parent;
        const size = Math.round(Math.max(clientWidth, clientHeight));
        let position = {
          top: "0px",
          left: "0px"
        };
        if (!this.center) {
          const rect = this.parent.getBoundingClientRect();
          const top = $event.pageY;
          const left = $event.pageX;
          position = {
            top: `${top - rect.top - size / 2 - document.documentElement.scrollTop}px`,
            left: `${left - rect.left - size / 2 - document.documentElement.scrollLeft}px`
          };
        }
        this.styles = __spreadProps$1(__spreadValues$1({}, position), {
          height: `${size}px`,
          width: `${size}px`
        });
        this.pressed = true;
        requestAnimationFrame(() => {
          this.active = true;
          clearTimeout(this.failsafe);
          this.failsafe = setTimeout(() => {
            this.pressed = false;
            this.active = false;
          }, 380 + 100 + 20);
        });
      });
    }
  },
  render() {
    if (this.pressed === false) {
      return void 0;
    }
    return h$1("div", {
      style: __spreadValues$1({}, this.styles),
      class: ["ripple", { active: this.active, center: this.center }],
      onTransitionEnd: () => {
        this.pressed = false;
        this.active = false;
      },
      onTransitionCancel: () => {
        this.pressed = false;
        this.active = false;
      }
    });
  },
  mounted() {
    this.parent.addEventListener("click", this.onClick);
    const parentStyles = window.getComputedStyle(this.parent);
    this.parent.style.overflow = "hidden";
    if (parentStyles.position === "relative" || parentStyles.position === "absolute" || parentStyles.position === "fixed") {
      return;
    }
    this.parent.style.position = "relative";
  },
  unmounted() {
    this.parent.removeEventListener("click", this.onClick);
  }
};
var FeatherRipple = /* @__PURE__ */ _export_sfc$2(_sfc_main$3, [["__scopeId", "data-v-1b7e908c"]]);
var __defProp = Object.defineProperty;
var __defProps = Object.defineProperties;
var __getOwnPropDescs = Object.getOwnPropertyDescriptors;
var __getOwnPropSymbols = Object.getOwnPropertySymbols;
var __hasOwnProp = Object.prototype.hasOwnProperty;
var __propIsEnum = Object.prototype.propertyIsEnumerable;
var __defNormalProp = (obj, key, value) => key in obj ? __defProp(obj, key, { enumerable: true, configurable: true, writable: true, value }) : obj[key] = value;
var __spreadValues = (a, b) => {
  for (var prop in b || (b = {}))
    if (__hasOwnProp.call(b, prop))
      __defNormalProp(a, prop, b[prop]);
  if (__getOwnPropSymbols)
    for (var prop of __getOwnPropSymbols(b)) {
      if (__propIsEnum.call(b, prop))
        __defNormalProp(a, prop, b[prop]);
    }
  return a;
};
var __spreadProps = (a, b) => __defProps(a, __getOwnPropDescs(b));
const h = window["Vue"].h;
var _export_sfc$1 = (sfc, props) => {
  const target = sfc.__vccOpts || sfc;
  for (const [key, val] of props) {
    target[key] = val;
  }
  return target;
};
const _sfc_main$2 = {
  inheritAttrs: false,
  props: {
    primary: {
      type: Boolean,
      default: false
    },
    text: {
      type: Boolean,
      default: false
    },
    secondary: {
      type: Boolean,
      default: false
    },
    icon: {
      type: String
    },
    disabled: {
      type: Boolean,
      default: false
    },
    asAnchor: {
      type: Boolean,
      default: false
    },
    onColor: {
      type: Boolean,
      default: false
    }
  },
  render() {
    const getClasses = () => {
      let buttonClass = "";
      if (this.primary) {
        buttonClass = "btn-primary";
      }
      if (this.secondary) {
        buttonClass = "btn-secondary";
      }
      if (this.text || this.icon) {
        buttonClass = "btn-text";
      }
      const classArr = ["btn", "hover", "focus", buttonClass];
      if (this.icon) {
        classArr.push("btn-icon");
        classArr.push("btn-icon-table");
      }
      if (this.onColor) {
        classArr.push("on-color");
      }
      return classArr;
    };
    const tag = this.asAnchor ? "a" : "button";
    const data = {};
    const _attrs = JSON.parse(JSON.stringify(this.$attrs));
    data.attrs = _attrs || {};
    if (this.asAnchor) {
      data.attrs.role = "button";
    } else {
      data.attrs.type = data.attrs.type || "button";
    }
    if (this.disabled) {
      data.attrs["aria-disabled"] = "true";
    }
    data.on = {
      onClick: (e) => {
        if (this.disabled) {
          this.$emit("disabled-click", e);
        } else {
          this.$emit("click", e);
        }
      }
    };
    const classes = getClasses();
    data.class = [this.$attrs.class].concat(classes);
    if (this.$slots.icon) {
      data.class.push("has-icon");
    }
    let ripple = h(FeatherRipple);
    if (this.disabled) {
      ripple = void 0;
    }
    if (this.icon && this.$slots.default) {
      const label = this.icon;
      data.attrs["aria-label"] = label;
      data.attrs["title"] = label;
      return h(tag, __spreadProps(__spreadValues(__spreadValues({}, data.attrs), data.on), { class: data.class }), [
        this.$slots.default(),
        this.disabled ? void 0 : h(FeatherRipple, { center: true })
      ]);
    }
    const content = h("span", { class: ["btn-content"] }, [
      this.$slots.default ? this.$slots.default() : ""
    ]);
    return h(tag, __spreadProps(__spreadValues(__spreadValues({}, data.attrs), data.on), { class: data.class }), [
      this.$slots.icon ? this.$slots.icon() : void 0,
      content,
      ripple
    ]);
  }
};
var FeatherButton = /* @__PURE__ */ _export_sfc$1(_sfc_main$2, [["__scopeId", "data-v-f808d47e"]]);
var TestPluginComponent_vue_vue_type_style_index_0_scoped_true_lang = "";
var _export_sfc = (sfc, props) => {
  const target = sfc.__vccOpts || sfc;
  for (const [key, val] of props) {
    target[key] = val;
  }
  return target;
};
const _defineComponent$1 = window["Vue"].defineComponent;
const _createTextVNode = window["Vue"].createTextVNode;
const _unref = window["Vue"].unref;
const _withCtx = window["Vue"].withCtx;
const _createVNode = window["Vue"].createVNode;
const _createElementVNode = window["Vue"].createElementVNode;
const _Fragment = window["Vue"].Fragment;
const _openBlock$1 = window["Vue"].openBlock;
const _createElementBlock = window["Vue"].createElementBlock;
window["Vue"].pushScopeId;
window["Vue"].popScopeId;
const _hoisted_1 = /* @__PURE__ */ _createTextVNode("Submit Primary Button");
const _sfc_main$1 = /* @__PURE__ */ _defineComponent$1({
  setup(__props) {
    const clickMe = () => {
      console.log("Clicked the red plugin component!");
    };
    return (_ctx, _cache) => {
      return _openBlock$1(), _createElementBlock(_Fragment, null, [
        _createVNode(_unref(FeatherButton), {
          primary: "",
          type: "submit"
        }, {
          default: _withCtx(() => [
            _hoisted_1
          ]),
          _: 1
        }),
        _createVNode(_unref(FeatherInput), { label: "Test input" }),
        _createElementVNode("div", {
          class: "title",
          onClick: clickMe
        }, "This is the red plugin. Click to see a console log.")
      ], 64);
    };
  }
});
var Test = /* @__PURE__ */ _export_sfc(_sfc_main$1, [["__scopeId", "data-v-3044a110"]]);
const _defineComponent = window["Vue"].defineComponent;
const _openBlock = window["Vue"].openBlock;
const _createBlock = window["Vue"].createBlock;
const _sfc_main = /* @__PURE__ */ _defineComponent({
  setup(__props) {
    return (_ctx, _cache) => {
      return _openBlock(), _createBlock(Test);
    };
  }
});
var style = "";
var openLight = "";
window["RedPlugin"] = _sfc_main;

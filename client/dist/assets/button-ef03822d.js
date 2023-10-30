import{i as t,c as o,S as r,x as e,_ as n,a as s,w as a,b as l,T as i,d as u,e as d,r as c,n as b,f as h}from"./chunk.2HU27ZWH-5a69d77d.js";var p=t`
  ${o}

  :host {
    --track-width: 2px;
    --track-color: rgb(128 128 128 / 25%);
    --indicator-color: var(--sl-color-primary-600);
    --speed: 2s;

    display: inline-flex;
    width: 1em;
    height: 1em;
  }

  .spinner {
    flex: 1 1 auto;
    height: 100%;
    width: 100%;
  }

  .spinner__track,
  .spinner__indicator {
    fill: none;
    stroke-width: var(--track-width);
    r: calc(0.5em - var(--track-width) / 2);
    cx: 0.5em;
    cy: 0.5em;
    transform-origin: 50% 50%;
  }

  .spinner__track {
    stroke: var(--track-color);
    transform-origin: 0% 0%;
  }

  .spinner__indicator {
    stroke: var(--indicator-color);
    stroke-linecap: round;
    stroke-dasharray: 150% 75%;
    animation: spin var(--speed) linear infinite;
  }

  @keyframes spin {
    0% {
      transform: rotate(0deg);
      stroke-dasharray: 0.01em, 2.75em;
    }

    50% {
      transform: rotate(450deg);
      stroke-dasharray: 1.375em, 1.375em;
    }

    100% {
      transform: rotate(1080deg);
      stroke-dasharray: 0.01em, 2.75em;
    }
  }
`;const m=new Set,g=new MutationObserver(w),v=new Map;let f,y=document.documentElement.dir||"ltr",_=document.documentElement.lang||navigator.language;function w(){y=document.documentElement.dir||"ltr",_=document.documentElement.lang||navigator.language,[...m.keys()].map((t=>{"function"==typeof t.requestUpdate&&t.requestUpdate()}))}g.observe(document.documentElement,{attributes:!0,attributeFilter:["dir","lang"]});let k=class{constructor(t){this.host=t,this.host.addController(this)}hostConnected(){m.add(this.host)}hostDisconnected(){m.delete(this.host)}dir(){return`${this.host.dir||y}`.toLowerCase()}lang(){return`${this.host.lang||_}`.toLowerCase()}getTranslationData(t){var o,r;const e=new Intl.Locale(t.replace(/_/g,"-")),n=null==e?void 0:e.language.toLowerCase(),s=null!==(r=null===(o=null==e?void 0:e.region)||void 0===o?void 0:o.toLowerCase())&&void 0!==r?r:"";return{locale:e,language:n,region:s,primary:v.get(`${n}-${s}`),secondary:v.get(n)}}exists(t,o){var r;const{primary:e,secondary:n}=this.getTranslationData(null!==(r=o.lang)&&void 0!==r?r:this.lang());return o=Object.assign({includeFallback:!1},o),!!(e&&e[t]||n&&n[t]||o.includeFallback&&f&&f[t])}term(t,...o){const{primary:r,secondary:e}=this.getTranslationData(this.lang());let n;if(r&&r[t])n=r[t];else if(e&&e[t])n=e[t];else{if(!f||!f[t])return console.error(`No translation found for: ${String(t)}`),String(t);n=f[t]}return"function"==typeof n?n(...o):n}date(t,o){return t=new Date(t),new Intl.DateTimeFormat(this.lang(),o).format(t)}number(t,o){return t=Number(t),isNaN(t)?"":new Intl.NumberFormat(this.lang(),o).format(t)}relativeTime(t,o,r){return new Intl.RelativeTimeFormat(this.lang(),r).format(t,o)}};var x=class extends k{};
/**
 * @license
 * Copyright 2017 Google LLC
 * SPDX-License-Identifier: BSD-3-Clause
 */const $=(t,o,r)=>(r.configurable=!0,r.enumerable=!0,Reflect.decorate&&"object"!=typeof o&&Object.defineProperty(t,o,r),r)
/**
 * @license
 * Copyright 2017 Google LLC
 * SPDX-License-Identifier: BSD-3-Clause
 */;var C=class extends r{constructor(){super(...arguments),this.localize=new x(this)}render(){return e`
      <svg part="base" class="spinner" role="progressbar" aria-label=${this.localize.term("loading")}>
        <circle class="spinner__track"></circle>
        <circle class="spinner__indicator"></circle>
      </svg>
    `}};C.styles=p;var S=new WeakMap,E=new WeakMap,F=new WeakSet,V=new WeakMap,A=class{constructor(t,o){this.handleFormData=t=>{const o=this.options.disabled(this.host),r=this.options.name(this.host),e=this.options.value(this.host),n="sl-button"===this.host.tagName.toLowerCase();!o&&!n&&"string"==typeof r&&r.length>0&&void 0!==e&&(Array.isArray(e)?e.forEach((o=>{t.formData.append(r,o.toString())})):t.formData.append(r,e.toString()))},this.handleFormSubmit=t=>{var o;const r=this.options.disabled(this.host),e=this.options.reportValidity;this.form&&!this.form.noValidate&&(null==(o=S.get(this.form))||o.forEach((t=>{this.setUserInteracted(t,!0)}))),!this.form||this.form.noValidate||r||e(this.host)||(t.preventDefault(),t.stopImmediatePropagation())},this.handleFormReset=()=>{this.options.setValue(this.host,this.options.defaultValue(this.host)),this.setUserInteracted(this.host,!1),V.set(this.host,[])},this.handleInteraction=t=>{const o=V.get(this.host);o.includes(t.type)||o.push(t.type),o.length===this.options.assumeInteractionOn.length&&this.setUserInteracted(this.host,!0)},this.reportFormValidity=()=>{if(this.form&&!this.form.noValidate){const t=this.form.querySelectorAll("*");for(const o of t)if("function"==typeof o.reportValidity&&!o.reportValidity())return!1}return!0},(this.host=t).addController(this),this.options=s({form:t=>{if(t.hasAttribute("form")&&""!==t.getAttribute("form")){const o=t.getRootNode(),r=t.getAttribute("form");if(r)return o.getElementById(r)}return t.closest("form")},name:t=>t.name,value:t=>t.value,defaultValue:t=>t.defaultValue,disabled:t=>{var o;return null!=(o=t.disabled)&&o},reportValidity:t=>"function"!=typeof t.reportValidity||t.reportValidity(),setValue:(t,o)=>t.value=o,assumeInteractionOn:["sl-input"]},o)}hostConnected(){const t=this.options.form(this.host);t&&this.attachForm(t),V.set(this.host,[]),this.options.assumeInteractionOn.forEach((t=>{this.host.addEventListener(t,this.handleInteraction)}))}hostDisconnected(){this.detachForm(),V.delete(this.host),this.options.assumeInteractionOn.forEach((t=>{this.host.removeEventListener(t,this.handleInteraction)}))}hostUpdated(){const t=this.options.form(this.host);t||this.detachForm(),t&&this.form!==t&&(this.detachForm(),this.attachForm(t)),this.host.hasUpdated&&this.setValidity(this.host.validity.valid)}attachForm(t){t?(this.form=t,S.has(this.form)?S.get(this.form).add(this.host):S.set(this.form,new Set([this.host])),this.form.addEventListener("formdata",this.handleFormData),this.form.addEventListener("submit",this.handleFormSubmit),this.form.addEventListener("reset",this.handleFormReset),E.has(this.form)||(E.set(this.form,this.form.reportValidity),this.form.reportValidity=()=>this.reportFormValidity())):this.form=void 0}detachForm(){var t;this.form&&(null==(t=S.get(this.form))||t.delete(this.host),this.form.removeEventListener("formdata",this.handleFormData),this.form.removeEventListener("submit",this.handleFormSubmit),this.form.removeEventListener("reset",this.handleFormReset),E.has(this.form)&&(this.form.reportValidity=E.get(this.form),E.delete(this.form))),this.form=void 0}setUserInteracted(t,o){o?F.add(t):F.delete(t),t.requestUpdate()}doAction(t,o){if(this.form){const r=document.createElement("button");r.type=t,r.style.position="absolute",r.style.width="0",r.style.height="0",r.style.clipPath="inset(50%)",r.style.overflow="hidden",r.style.whiteSpace="nowrap",o&&(r.name=o.name,r.value=o.value,["formaction","formenctype","formmethod","formnovalidate","formtarget"].forEach((t=>{o.hasAttribute(t)&&r.setAttribute(t,o.getAttribute(t))}))),this.form.append(r),r.click(),r.remove()}}getForm(){var t;return null!=(t=this.form)?t:null}reset(t){this.doAction("reset",t)}submit(t){this.doAction("submit",t)}setValidity(t){const o=this.host,r=Boolean(F.has(o)),e=Boolean(o.required);o.toggleAttribute("data-required",e),o.toggleAttribute("data-optional",!e),o.toggleAttribute("data-invalid",!t),o.toggleAttribute("data-valid",t),o.toggleAttribute("data-user-invalid",!t&&r),o.toggleAttribute("data-user-valid",t&&r)}updateValidity(){const t=this.host;this.setValidity(t.validity.valid)}emitInvalidEvent(t){const o=new CustomEvent("sl-invalid",{bubbles:!1,composed:!1,cancelable:!0,detail:{}});t||o.preventDefault(),this.host.dispatchEvent(o)||null==t||t.preventDefault()}},D=Object.freeze({badInput:!1,customError:!1,patternMismatch:!1,rangeOverflow:!1,rangeUnderflow:!1,stepMismatch:!1,tooLong:!1,tooShort:!1,typeMismatch:!1,valid:!0,valueMissing:!1});Object.freeze(n(s({},D),{valid:!1,valueMissing:!0})),Object.freeze(n(s({},D),{valid:!1,customError:!0}));var B=t`
  ${o}

  :host {
    display: inline-block;
    position: relative;
    width: auto;
    cursor: pointer;
  }

  .button {
    display: inline-flex;
    align-items: stretch;
    justify-content: center;
    width: 100%;
    border-style: solid;
    border-width: var(--sl-input-border-width);
    font-family: var(--sl-input-font-family);
    font-weight: var(--sl-font-weight-semibold);
    text-decoration: none;
    user-select: none;
    -webkit-user-select: none;
    white-space: nowrap;
    vertical-align: middle;
    padding: 0;
    transition:
      var(--sl-transition-x-fast) background-color,
      var(--sl-transition-x-fast) color,
      var(--sl-transition-x-fast) border,
      var(--sl-transition-x-fast) box-shadow;
    cursor: inherit;
  }

  .button::-moz-focus-inner {
    border: 0;
  }

  .button:focus {
    outline: none;
  }

  .button:focus-visible {
    outline: var(--sl-focus-ring);
    outline-offset: var(--sl-focus-ring-offset);
  }

  .button--disabled {
    opacity: 0.5;
    cursor: not-allowed;
  }

  /* When disabled, prevent mouse events from bubbling up from children */
  .button--disabled * {
    pointer-events: none;
  }

  .button__prefix,
  .button__suffix {
    flex: 0 0 auto;
    display: flex;
    align-items: center;
    pointer-events: none;
  }

  .button__label {
    display: inline-block;
  }

  .button__label::slotted(sl-icon) {
    vertical-align: -2px;
  }

  /*
   * Standard buttons
   */

  /* Default */
  .button--standard.button--default {
    background-color: var(--sl-color-neutral-0);
    border-color: var(--sl-color-neutral-300);
    color: var(--sl-color-neutral-700);
  }

  .button--standard.button--default:hover:not(.button--disabled) {
    background-color: var(--sl-color-primary-50);
    border-color: var(--sl-color-primary-300);
    color: var(--sl-color-primary-700);
  }

  .button--standard.button--default:active:not(.button--disabled) {
    background-color: var(--sl-color-primary-100);
    border-color: var(--sl-color-primary-400);
    color: var(--sl-color-primary-700);
  }

  /* Primary */
  .button--standard.button--primary {
    background-color: var(--sl-color-primary-600);
    border-color: var(--sl-color-primary-600);
    color: var(--sl-color-neutral-0);
  }

  .button--standard.button--primary:hover:not(.button--disabled) {
    background-color: var(--sl-color-primary-500);
    border-color: var(--sl-color-primary-500);
    color: var(--sl-color-neutral-0);
  }

  .button--standard.button--primary:active:not(.button--disabled) {
    background-color: var(--sl-color-primary-600);
    border-color: var(--sl-color-primary-600);
    color: var(--sl-color-neutral-0);
  }

  /* Success */
  .button--standard.button--success {
    background-color: var(--sl-color-success-600);
    border-color: var(--sl-color-success-600);
    color: var(--sl-color-neutral-0);
  }

  .button--standard.button--success:hover:not(.button--disabled) {
    background-color: var(--sl-color-success-500);
    border-color: var(--sl-color-success-500);
    color: var(--sl-color-neutral-0);
  }

  .button--standard.button--success:active:not(.button--disabled) {
    background-color: var(--sl-color-success-600);
    border-color: var(--sl-color-success-600);
    color: var(--sl-color-neutral-0);
  }

  /* Neutral */
  .button--standard.button--neutral {
    background-color: var(--sl-color-neutral-600);
    border-color: var(--sl-color-neutral-600);
    color: var(--sl-color-neutral-0);
  }

  .button--standard.button--neutral:hover:not(.button--disabled) {
    background-color: var(--sl-color-neutral-500);
    border-color: var(--sl-color-neutral-500);
    color: var(--sl-color-neutral-0);
  }

  .button--standard.button--neutral:active:not(.button--disabled) {
    background-color: var(--sl-color-neutral-600);
    border-color: var(--sl-color-neutral-600);
    color: var(--sl-color-neutral-0);
  }

  /* Warning */
  .button--standard.button--warning {
    background-color: var(--sl-color-warning-600);
    border-color: var(--sl-color-warning-600);
    color: var(--sl-color-neutral-0);
  }
  .button--standard.button--warning:hover:not(.button--disabled) {
    background-color: var(--sl-color-warning-500);
    border-color: var(--sl-color-warning-500);
    color: var(--sl-color-neutral-0);
  }

  .button--standard.button--warning:active:not(.button--disabled) {
    background-color: var(--sl-color-warning-600);
    border-color: var(--sl-color-warning-600);
    color: var(--sl-color-neutral-0);
  }

  /* Danger */
  .button--standard.button--danger {
    background-color: var(--sl-color-danger-600);
    border-color: var(--sl-color-danger-600);
    color: var(--sl-color-neutral-0);
  }

  .button--standard.button--danger:hover:not(.button--disabled) {
    background-color: var(--sl-color-danger-500);
    border-color: var(--sl-color-danger-500);
    color: var(--sl-color-neutral-0);
  }

  .button--standard.button--danger:active:not(.button--disabled) {
    background-color: var(--sl-color-danger-600);
    border-color: var(--sl-color-danger-600);
    color: var(--sl-color-neutral-0);
  }

  /*
   * Outline buttons
   */

  .button--outline {
    background: none;
    border: solid 1px;
  }

  /* Default */
  .button--outline.button--default {
    border-color: var(--sl-color-neutral-300);
    color: var(--sl-color-neutral-700);
  }

  .button--outline.button--default:hover:not(.button--disabled),
  .button--outline.button--default.button--checked:not(.button--disabled) {
    border-color: var(--sl-color-primary-600);
    background-color: var(--sl-color-primary-600);
    color: var(--sl-color-neutral-0);
  }

  .button--outline.button--default:active:not(.button--disabled) {
    border-color: var(--sl-color-primary-700);
    background-color: var(--sl-color-primary-700);
    color: var(--sl-color-neutral-0);
  }

  /* Primary */
  .button--outline.button--primary {
    border-color: var(--sl-color-primary-600);
    color: var(--sl-color-primary-600);
  }

  .button--outline.button--primary:hover:not(.button--disabled),
  .button--outline.button--primary.button--checked:not(.button--disabled) {
    background-color: var(--sl-color-primary-600);
    color: var(--sl-color-neutral-0);
  }

  .button--outline.button--primary:active:not(.button--disabled) {
    border-color: var(--sl-color-primary-700);
    background-color: var(--sl-color-primary-700);
    color: var(--sl-color-neutral-0);
  }

  /* Success */
  .button--outline.button--success {
    border-color: var(--sl-color-success-600);
    color: var(--sl-color-success-600);
  }

  .button--outline.button--success:hover:not(.button--disabled),
  .button--outline.button--success.button--checked:not(.button--disabled) {
    background-color: var(--sl-color-success-600);
    color: var(--sl-color-neutral-0);
  }

  .button--outline.button--success:active:not(.button--disabled) {
    border-color: var(--sl-color-success-700);
    background-color: var(--sl-color-success-700);
    color: var(--sl-color-neutral-0);
  }

  /* Neutral */
  .button--outline.button--neutral {
    border-color: var(--sl-color-neutral-600);
    color: var(--sl-color-neutral-600);
  }

  .button--outline.button--neutral:hover:not(.button--disabled),
  .button--outline.button--neutral.button--checked:not(.button--disabled) {
    background-color: var(--sl-color-neutral-600);
    color: var(--sl-color-neutral-0);
  }

  .button--outline.button--neutral:active:not(.button--disabled) {
    border-color: var(--sl-color-neutral-700);
    background-color: var(--sl-color-neutral-700);
    color: var(--sl-color-neutral-0);
  }

  /* Warning */
  .button--outline.button--warning {
    border-color: var(--sl-color-warning-600);
    color: var(--sl-color-warning-600);
  }

  .button--outline.button--warning:hover:not(.button--disabled),
  .button--outline.button--warning.button--checked:not(.button--disabled) {
    background-color: var(--sl-color-warning-600);
    color: var(--sl-color-neutral-0);
  }

  .button--outline.button--warning:active:not(.button--disabled) {
    border-color: var(--sl-color-warning-700);
    background-color: var(--sl-color-warning-700);
    color: var(--sl-color-neutral-0);
  }

  /* Danger */
  .button--outline.button--danger {
    border-color: var(--sl-color-danger-600);
    color: var(--sl-color-danger-600);
  }

  .button--outline.button--danger:hover:not(.button--disabled),
  .button--outline.button--danger.button--checked:not(.button--disabled) {
    background-color: var(--sl-color-danger-600);
    color: var(--sl-color-neutral-0);
  }

  .button--outline.button--danger:active:not(.button--disabled) {
    border-color: var(--sl-color-danger-700);
    background-color: var(--sl-color-danger-700);
    color: var(--sl-color-neutral-0);
  }

  @media (forced-colors: active) {
    .button.button--outline.button--checked:not(.button--disabled) {
      outline: solid 2px transparent;
    }
  }

  /*
   * Text buttons
   */

  .button--text {
    background-color: transparent;
    border-color: transparent;
    color: var(--sl-color-primary-600);
  }

  .button--text:hover:not(.button--disabled) {
    background-color: transparent;
    border-color: transparent;
    color: var(--sl-color-primary-500);
  }

  .button--text:focus-visible:not(.button--disabled) {
    background-color: transparent;
    border-color: transparent;
    color: var(--sl-color-primary-500);
  }

  .button--text:active:not(.button--disabled) {
    background-color: transparent;
    border-color: transparent;
    color: var(--sl-color-primary-700);
  }

  /*
   * Size modifiers
   */

  .button--small {
    height: auto;
    min-height: var(--sl-input-height-small);
    font-size: var(--sl-button-font-size-small);
    line-height: calc(var(--sl-input-height-small) - var(--sl-input-border-width) * 2);
    border-radius: var(--sl-input-border-radius-small);
  }

  .button--medium {
    height: auto;
    min-height: var(--sl-input-height-medium);
    font-size: var(--sl-button-font-size-medium);
    line-height: calc(var(--sl-input-height-medium) - var(--sl-input-border-width) * 2);
    border-radius: var(--sl-input-border-radius-medium);
  }

  .button--large {
    height: auto;
    min-height: var(--sl-input-height-large);
    font-size: var(--sl-button-font-size-large);
    line-height: calc(var(--sl-input-height-large) - var(--sl-input-border-width) * 2);
    border-radius: var(--sl-input-border-radius-large);
  }

  /*
   * Pill modifier
   */

  .button--pill.button--small {
    border-radius: var(--sl-input-height-small);
  }

  .button--pill.button--medium {
    border-radius: var(--sl-input-height-medium);
  }

  .button--pill.button--large {
    border-radius: var(--sl-input-height-large);
  }

  /*
   * Circle modifier
   */

  .button--circle {
    padding-left: 0;
    padding-right: 0;
  }

  .button--circle.button--small {
    width: var(--sl-input-height-small);
    border-radius: 50%;
  }

  .button--circle.button--medium {
    width: var(--sl-input-height-medium);
    border-radius: 50%;
  }

  .button--circle.button--large {
    width: var(--sl-input-height-large);
    border-radius: 50%;
  }

  .button--circle .button__prefix,
  .button--circle .button__suffix,
  .button--circle .button__caret {
    display: none;
  }

  /*
   * Caret modifier
   */

  .button--caret .button__suffix {
    display: none;
  }

  .button--caret .button__caret {
    height: auto;
  }

  /*
   * Loading modifier
   */

  .button--loading {
    position: relative;
    cursor: wait;
  }

  .button--loading .button__prefix,
  .button--loading .button__label,
  .button--loading .button__suffix,
  .button--loading .button__caret {
    visibility: hidden;
  }

  .button--loading sl-spinner {
    --indicator-color: currentColor;
    position: absolute;
    font-size: 1em;
    height: 1em;
    width: 1em;
    top: calc(50% - 0.5em);
    left: calc(50% - 0.5em);
  }

  /*
   * Badges
   */

  .button ::slotted(sl-badge) {
    position: absolute;
    top: 0;
    right: 0;
    translate: 50% -50%;
    pointer-events: none;
  }

  .button--rtl ::slotted(sl-badge) {
    right: auto;
    left: 0;
    translate: -50% -50%;
  }

  /*
   * Button spacing
   */

  .button--has-label.button--small .button__label {
    padding: 0 var(--sl-spacing-small);
  }

  .button--has-label.button--medium .button__label {
    padding: 0 var(--sl-spacing-medium);
  }

  .button--has-label.button--large .button__label {
    padding: 0 var(--sl-spacing-large);
  }

  .button--has-prefix.button--small {
    padding-inline-start: var(--sl-spacing-x-small);
  }

  .button--has-prefix.button--small .button__label {
    padding-inline-start: var(--sl-spacing-x-small);
  }

  .button--has-prefix.button--medium {
    padding-inline-start: var(--sl-spacing-small);
  }

  .button--has-prefix.button--medium .button__label {
    padding-inline-start: var(--sl-spacing-small);
  }

  .button--has-prefix.button--large {
    padding-inline-start: var(--sl-spacing-small);
  }

  .button--has-prefix.button--large .button__label {
    padding-inline-start: var(--sl-spacing-small);
  }

  .button--has-suffix.button--small,
  .button--caret.button--small {
    padding-inline-end: var(--sl-spacing-x-small);
  }

  .button--has-suffix.button--small .button__label,
  .button--caret.button--small .button__label {
    padding-inline-end: var(--sl-spacing-x-small);
  }

  .button--has-suffix.button--medium,
  .button--caret.button--medium {
    padding-inline-end: var(--sl-spacing-small);
  }

  .button--has-suffix.button--medium .button__label,
  .button--caret.button--medium .button__label {
    padding-inline-end: var(--sl-spacing-small);
  }

  .button--has-suffix.button--large,
  .button--caret.button--large {
    padding-inline-end: var(--sl-spacing-small);
  }

  .button--has-suffix.button--large .button__label,
  .button--caret.button--large .button__label {
    padding-inline-end: var(--sl-spacing-small);
  }

  /*
   * Button groups support a variety of button types (e.g. buttons with tooltips, buttons as dropdown triggers, etc.).
   * This means buttons aren't always direct descendants of the button group, thus we can't target them with the
   * ::slotted selector. To work around this, the button group component does some magic to add these special classes to
   * buttons and we style them here instead.
   */

  :host(.sl-button-group__button--first:not(.sl-button-group__button--last)) .button {
    border-start-end-radius: 0;
    border-end-end-radius: 0;
  }

  :host(.sl-button-group__button--inner) .button {
    border-radius: 0;
  }

  :host(.sl-button-group__button--last:not(.sl-button-group__button--first)) .button {
    border-start-start-radius: 0;
    border-end-start-radius: 0;
  }

  /* All except the first */
  :host(.sl-button-group__button:not(.sl-button-group__button--first)) {
    margin-inline-start: calc(-1 * var(--sl-input-border-width));
  }

  /* Add a visual separator between solid buttons */
  :host(
      .sl-button-group__button:not(
          .sl-button-group__button--first,
          .sl-button-group__button--radio,
          [variant='default']
        ):not(:hover)
    )
    .button:after {
    content: '';
    position: absolute;
    top: 0;
    inset-inline-start: 0;
    bottom: 0;
    border-left: solid 1px rgb(128 128 128 / 33%);
    mix-blend-mode: multiply;
  }

  /* Bump hovered, focused, and checked buttons up so their focus ring isn't clipped */
  :host(.sl-button-group__button--hover) {
    z-index: 1;
  }

  /* Focus and checked are always on top */
  :host(.sl-button-group__button--focus),
  :host(.sl-button-group__button[checked]) {
    z-index: 2;
  }
`,z=class{constructor(t,...o){this.slotNames=[],this.handleSlotChange=t=>{const o=t.target;(this.slotNames.includes("[default]")&&!o.name||o.name&&this.slotNames.includes(o.name))&&this.host.requestUpdate()},(this.host=t).addController(this),this.slotNames=o}hasDefaultSlot(){return[...this.host.childNodes].some((t=>{if(t.nodeType===t.TEXT_NODE&&""!==t.textContent.trim())return!0;if(t.nodeType===t.ELEMENT_NODE){const o=t;if("sl-visually-hidden"===o.tagName.toLowerCase())return!1;if(!o.hasAttribute("slot"))return!0}return!1}))}hasNamedSlot(t){return null!==this.host.querySelector(`:scope > [slot="${t}"]`)}test(t){return"[default]"===t?this.hasDefaultSlot():this.hasNamedSlot(t)}hostConnected(){this.host.shadowRoot.addEventListener("slotchange",this.handleSlotChange)}hostDisconnected(){this.host.shadowRoot.removeEventListener("slotchange",this.handleSlotChange)}};
/**
 * @license
 * Copyright 2017 Google LLC
 * SPDX-License-Identifier: BSD-3-Clause
 */
const L=1;class N{constructor(t){}get _$AU(){return this._$AM._$AU}_$AT(t,o,r){this._$Ct=t,this._$AM=o,this._$Ci=r}_$AS(t,o){return this.update(t,o)}update(t,o){return this.render(...o)}}
/**
 * @license
 * Copyright 2018 Google LLC
 * SPDX-License-Identifier: BSD-3-Clause
 */const I=(T=class extends N{constructor(t){var o;if(super(t),t.type!==L||"class"!==t.name||(null==(o=t.strings)?void 0:o.length)>2)throw Error("`classMap()` can only be used in the `class` attribute and must be the only part in the attribute.")}render(t){return" "+Object.keys(t).filter((o=>t[o])).join(" ")+" "}update(t,[o]){var r,e;if(void 0===this.it){this.it=new Set,void 0!==t.strings&&(this.st=new Set(t.strings.join(" ").split(/\s/).filter((t=>""!==t))));for(const t in o)o[t]&&!(null==(r=this.st)?void 0:r.has(t))&&this.it.add(t);return this.render(o)}const n=t.element.classList;for(const s of this.it)s in o||(n.remove(s),this.it.delete(s));for(const s in o){const t=!!o[s];t===this.it.has(s)||(null==(e=this.st)?void 0:e.has(s))||(t?(n.add(s),this.it.add(s)):(n.remove(s),this.it.delete(s)))}return a}},(...t)=>({_$litDirective$:T,values:t}));
/**
 * @license
 * Copyright 2020 Google LLC
 * SPDX-License-Identifier: BSD-3-Clause
 */var T;const O=Symbol.for(""),M=t=>{if((null==t?void 0:t.r)===O)return null==t?void 0:t._$litStatic$},U=(t,...o)=>({_$litStatic$:o.reduce(((o,r,e)=>o+(t=>{if(void 0!==t._$litStatic$)return t._$litStatic$;throw Error(`Value passed to 'literal' function must be a 'literal' result: ${t}. Use 'unsafeStatic' to pass non-literal values, but\n            take care to ensure page security.`)})(r)+t[e+1]),t[0]),r:O}),j=new Map,R=(t=>(o,...r)=>{const e=r.length;let n,s;const a=[],l=[];let i,u=0,d=!1;for(;u<e;){for(i=o[u];u<e&&void 0!==(s=r[u],n=M(s));)i+=n+o[++u],d=!0;u!==e&&l.push(s),a.push(i),u++}if(u===e&&a.push(o[e]),d){const t=a.join("$$lit$$");void 0===(o=j.get(t))&&(a.raw=a,j.set(t,o=a)),r=l}return t(o,...r)})(l),P=t=>t??i;
/**
 * @license
 * Copyright 2018 Google LLC
 * SPDX-License-Identifier: BSD-3-Clause
 */var q,W,H=class extends r{constructor(){super(...arguments),this.formControlController=new A(this,{form:t=>{if(t.hasAttribute("form")){const o=t.getRootNode(),r=t.getAttribute("form");return o.getElementById(r)}return t.closest("form")},assumeInteractionOn:["click"]}),this.hasSlotController=new z(this,"[default]","prefix","suffix"),this.localize=new x(this),this.hasFocus=!1,this.invalid=!1,this.title="",this.variant="default",this.size="medium",this.caret=!1,this.disabled=!1,this.loading=!1,this.outline=!1,this.pill=!1,this.circle=!1,this.type="button",this.name="",this.value="",this.href="",this.rel="noreferrer noopener"}get validity(){return this.isButton()?this.button.validity:D}get validationMessage(){return this.isButton()?this.button.validationMessage:""}firstUpdated(){this.isButton()&&this.formControlController.updateValidity()}handleBlur(){this.hasFocus=!1,this.emit("sl-blur")}handleFocus(){this.hasFocus=!0,this.emit("sl-focus")}handleClick(){"submit"===this.type&&this.formControlController.submit(this),"reset"===this.type&&this.formControlController.reset(this)}handleInvalid(t){this.formControlController.setValidity(!1),this.formControlController.emitInvalidEvent(t)}isButton(){return!this.href}isLink(){return!!this.href}handleDisabledChange(){this.isButton()&&this.formControlController.setValidity(this.disabled)}click(){this.button.click()}focus(t){this.button.focus(t)}blur(){this.button.blur()}checkValidity(){return!this.isButton()||this.button.checkValidity()}getForm(){return this.formControlController.getForm()}reportValidity(){return!this.isButton()||this.button.reportValidity()}setCustomValidity(t){this.isButton()&&(this.button.setCustomValidity(t),this.formControlController.updateValidity())}render(){const t=this.isLink(),o=t?U`a`:U`button`;return R`
      <${o}
        part="base"
        class=${I({button:!0,"button--default":"default"===this.variant,"button--primary":"primary"===this.variant,"button--success":"success"===this.variant,"button--neutral":"neutral"===this.variant,"button--warning":"warning"===this.variant,"button--danger":"danger"===this.variant,"button--text":"text"===this.variant,"button--small":"small"===this.size,"button--medium":"medium"===this.size,"button--large":"large"===this.size,"button--caret":this.caret,"button--circle":this.circle,"button--disabled":this.disabled,"button--focused":this.hasFocus,"button--loading":this.loading,"button--standard":!this.outline,"button--outline":this.outline,"button--pill":this.pill,"button--rtl":"rtl"===this.localize.dir(),"button--has-label":this.hasSlotController.test("[default]"),"button--has-prefix":this.hasSlotController.test("prefix"),"button--has-suffix":this.hasSlotController.test("suffix")})}
        ?disabled=${P(t?void 0:this.disabled)}
        type=${P(t?void 0:this.type)}
        title=${this.title}
        name=${P(t?void 0:this.name)}
        value=${P(t?void 0:this.value)}
        href=${P(t?this.href:void 0)}
        target=${P(t?this.target:void 0)}
        download=${P(t?this.download:void 0)}
        rel=${P(t?this.rel:void 0)}
        role=${P(t?void 0:"button")}
        aria-disabled=${this.disabled?"true":"false"}
        tabindex=${this.disabled?"-1":"0"}
        @blur=${this.handleBlur}
        @focus=${this.handleFocus}
        @invalid=${this.isButton()?this.handleInvalid:null}
        @click=${this.handleClick}
      >
        <slot name="prefix" part="prefix" class="button__prefix"></slot>
        <slot part="label" class="button__label"></slot>
        <slot name="suffix" part="suffix" class="button__suffix"></slot>
        ${this.caret?R` <sl-icon part="caret" class="button__caret" library="system" name="caret"></sl-icon> `:""}
        ${this.loading?R`<sl-spinner part="spinner"></sl-spinner>`:""}
      </${o}>
    `}};H.styles=B,H.dependencies={"sl-icon":u,"sl-spinner":C},d([(q=".button",(t,o,r)=>{const e=t=>{var o;return(null==(o=t.renderRoot)?void 0:o.querySelector(q))??null};if(W){const{get:n,set:s}="object"==typeof o?t:r??(()=>{const t=Symbol();return{get(){return this[t]},set(o){this[t]=o}}})();return $(t,o,{get(){if(W){let t=n.call(this);return void 0===t&&(t=e(this),s.call(this,t)),t}return e(this)}})}return $(t,o,{get(){return e(this)}})})],H.prototype,"button",2),d([c()],H.prototype,"hasFocus",2),d([c()],H.prototype,"invalid",2),d([b()],H.prototype,"title",2),d([b({reflect:!0})],H.prototype,"variant",2),d([b({reflect:!0})],H.prototype,"size",2),d([b({type:Boolean,reflect:!0})],H.prototype,"caret",2),d([b({type:Boolean,reflect:!0})],H.prototype,"disabled",2),d([b({type:Boolean,reflect:!0})],H.prototype,"loading",2),d([b({type:Boolean,reflect:!0})],H.prototype,"outline",2),d([b({type:Boolean,reflect:!0})],H.prototype,"pill",2),d([b({type:Boolean,reflect:!0})],H.prototype,"circle",2),d([b()],H.prototype,"type",2),d([b()],H.prototype,"name",2),d([b()],H.prototype,"value",2),d([b()],H.prototype,"href",2),d([b()],H.prototype,"target",2),d([b()],H.prototype,"rel",2),d([b()],H.prototype,"download",2),d([b()],H.prototype,"form",2),d([b({attribute:"formaction"})],H.prototype,"formAction",2),d([b({attribute:"formenctype"})],H.prototype,"formEnctype",2),d([b({attribute:"formmethod"})],H.prototype,"formMethod",2),d([b({attribute:"formnovalidate",type:Boolean})],H.prototype,"formNoValidate",2),d([b({attribute:"formtarget"})],H.prototype,"formTarget",2),d([h("disabled",{waitUntilFirstUpdate:!0})],H.prototype,"handleDisabledChange",1);var G=H;H.define("sl-button");!function(...t){t.map((t=>{const o=t.$code.toLowerCase();v.has(o)?v.set(o,Object.assign(Object.assign({},v.get(o)),t)):v.set(o,t),f||(f=t)})),w()}({$code:"en",$name:"English",$dir:"ltr",carousel:"Carousel",clearEntry:"Clear entry",close:"Close",copied:"Copied",copy:"Copy",currentValue:"Current value",error:"Error",goToSlide:(t,o)=>`Go to slide ${t} of ${o}`,hidePassword:"Hide password",loading:"Loading",nextSlide:"Next slide",numOptionsSelected:t=>0===t?"No options selected":1===t?"1 option selected":`${t} options selected`,previousSlide:"Previous slide",progress:"Progress",remove:"Remove",resize:"Resize",scrollToEnd:"Scroll to end",scrollToStart:"Scroll to start",selectAColorFromTheScreen:"Select a color from the screen",showPassword:"Show password",slideNum:t=>`Slide ${t}`,toggleColorFormat:"Toggle color format"});export{G as default};
//# sourceMappingURL=button-ef03822d.js.map

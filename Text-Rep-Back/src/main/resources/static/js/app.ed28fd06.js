(function(e){function t(t){for(var r,o,i=t[0],c=t[1],p=t[2],u=0,d=[];u<i.length;u++)o=i[u],Object.prototype.hasOwnProperty.call(a,o)&&a[o]&&d.push(a[o][0]),a[o]=0;for(r in c)Object.prototype.hasOwnProperty.call(c,r)&&(e[r]=c[r]);s&&s(t);while(d.length)d.shift()();return l.push.apply(l,p||[]),n()}function n(){for(var e,t=0;t<l.length;t++){for(var n=l[t],r=!0,i=1;i<n.length;i++){var c=n[i];0!==a[c]&&(r=!1)}r&&(l.splice(t--,1),e=o(o.s=n[0]))}return e}var r={},a={app:0},l=[];function o(t){if(r[t])return r[t].exports;var n=r[t]={i:t,l:!1,exports:{}};return e[t].call(n.exports,n,n.exports,o),n.l=!0,n.exports}o.m=e,o.c=r,o.d=function(e,t,n){o.o(e,t)||Object.defineProperty(e,t,{enumerable:!0,get:n})},o.r=function(e){"undefined"!==typeof Symbol&&Symbol.toStringTag&&Object.defineProperty(e,Symbol.toStringTag,{value:"Module"}),Object.defineProperty(e,"__esModule",{value:!0})},o.t=function(e,t){if(1&t&&(e=o(e)),8&t)return e;if(4&t&&"object"===typeof e&&e&&e.__esModule)return e;var n=Object.create(null);if(o.r(n),Object.defineProperty(n,"default",{enumerable:!0,value:e}),2&t&&"string"!=typeof e)for(var r in e)o.d(n,r,function(t){return e[t]}.bind(null,r));return n},o.n=function(e){var t=e&&e.__esModule?function(){return e["default"]}:function(){return e};return o.d(t,"a",t),t},o.o=function(e,t){return Object.prototype.hasOwnProperty.call(e,t)},o.p="/";var i=window["webpackJsonp"]=window["webpackJsonp"]||[],c=i.push.bind(i);i.push=t,i=i.slice();for(var p=0;p<i.length;p++)t(i[p]);var s=c;l.push([0,"chunk-vendors"]),n()})({0:function(e,t,n){e.exports=n("56d7")},"034f":function(e,t,n){"use strict";var r=n("64a9"),a=n.n(r);a.a},"17e6":function(e,t,n){},"56d7":function(e,t,n){"use strict";n.r(t);var r=n("2b0e"),a=function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("div",{attrs:{id:"app"}},[n("ReplaceText")],1)},l=[],o=function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("table",{staticClass:"replacetext",attrs:{border:"1"}},[n("tr",[n("upload-file",{staticClass:"upload-file",on:{replaceAndDownload:e.replaceAndDownload},model:{value:e.UploadFile,callback:function(t){e.UploadFile=t},expression:"UploadFile"}})],1)])},i=[],c=function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("div",{staticClass:"upload-file"},[n("div",{staticClass:"large-12 medium-12 small-12 cell"},[n("label",[e._v("File\n      "),n("input",{ref:"file",attrs:{type:"file",id:"file"},on:{change:function(t){return e.handleFileUpload()}}})]),n("label",[e._v("Search Text")]),n("input",{directives:[{name:"model",rawName:"v-model",value:e.searchText,expression:"searchText"}],attrs:{type:"text",placeholder:"Search Text"},domProps:{value:e.searchText},on:{input:function(t){t.target.composing||(e.searchText=t.target.value)}}}),n("label",[e._v("Replace Text")]),n("input",{directives:[{name:"model",rawName:"v-model",value:e.replaceText,expression:"replaceText"}],attrs:{type:"text",placeholder:"Replace Text"},domProps:{value:e.replaceText},on:{input:function(t){t.target.composing||(e.replaceText=t.target.value)}}}),n("button",{on:{click:function(t){return e.replaceAndDownload()}}},[e._v("Replace and Download")])])])},p=[],s=n("bc3a"),u=n.n(s),d={name:"UploadFile",data(){return{file:"",searchText:"",replaceText:""}},methods:{replaceAndDownload(){let e=new FormData;e.append("file",this.file),e.append("searchText",this.searchText),e.append("replaceText",this.replaceText),u.a.post("/api/uploadFile",e),window.location.reload()},handleFileUpload(){this.file=this.$refs.file.files[0]}}},f=d,h=n("2877"),v=Object(h["a"])(f,c,p,!1,null,null,null),m=v.exports,x={name:"ReplaceText",components:{UploadFile:m},data(){return{products:["Boots","Socks","Tie"],messages:["hello","vue","js"],shoppingItems:[{name:"apple",price:"10"},{name:"orange",price:"12"}]}},computed:{totalProducts(){return this.products.reduce((e,t)=>{return e+t.quantity},0)}},created(){fetch("https://api.myjson.com/bins/74l63").then(e=>e.json()).then(e=>{this.products=e.products})}},b=x,T=(n("69df"),Object(h["a"])(b,o,i,!1,null,"716bfeef",null)),g=T.exports,y={name:"App",components:{ReplaceText:g}},w=y,_=(n("034f"),Object(h["a"])(w,a,l,!1,null,null,null)),j=_.exports;r["a"].config.productionTip=!1,new r["a"]({render:e=>e(j)}).$mount("#app")},"64a9":function(e,t,n){},"69df":function(e,t,n){"use strict";var r=n("17e6"),a=n.n(r);a.a}});
//# sourceMappingURL=app.ed28fd06.js.map
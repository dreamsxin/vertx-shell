/*
 * Copyright 2014 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

/** @module vertx-shell-js/process_context */
var utils = require('vertx-js/util/utils');
var Tty = require('vertx-shell-js/tty');
var Session = require('vertx-shell-js/session');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JProcessContext = io.vertx.ext.shell.process.ProcessContext;

/**
 Allow a process to interact with its context during execution.

 @class
*/
var ProcessContext = function(j_val) {

  var j_processContext = j_val;
  var that = this;

  /**
   @return the tty assocated with this process

   @public

   @return {Tty}
   */
  this.tty = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnVertxGen(j_processContext["tty()"](), Tty);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   @return the shell session

   @public

   @return {Session}
   */
  this.session = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnVertxGen(j_processContext["session()"](), Session);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param handler {function} 
   */
  this.interruptHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_processContext["interruptHandler(io.vertx.core.Handler)"](handler);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param handler {function} 
   */
  this.suspendHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_processContext["suspendHandler(io.vertx.core.Handler)"](handler);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param handler {function} 
   */
  this.resumeHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_processContext["resumeHandler(io.vertx.core.Handler)"](handler);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   End the process.

   @public
   @param status {number} the termination status 
   */
  this.end = function(status) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] ==='number') {
      j_processContext["end(int)"](status);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_processContext;
};

// We export the Constructor function
module.exports = ProcessContext;
/*
 * Copyright 2015 Red Hat, Inc.
 *
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  The Apache License v2.0 is available at
 *  http://www.opensource.org/licenses/apache2.0.php
 *
 *  You may elect to redistribute this code under either of these licenses.
 *
 *
 * Copyright (c) 2015 The original author or authors
 * ------------------------------------------------------
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 *
 *     The Eclipse Public License is available at
 *     http://www.eclipse.org/legal/epl-v10.html
 *
 *     The Apache License v2.0 is available at
 *     http://www.opensource.org/licenses/apache2.0.php
 *
 * You may elect to redistribute this code under either of these licenses.
 *
 */

package io.vertx.ext.shell.term;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.shell.term.impl.SSHTermServer;
import io.vertx.ext.shell.term.impl.TelnetTermServer;

/**
 * A server for terminal based applications.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public interface TermServer {

  /**
   * Create a term server for the SSH protocol.
   *
   * @param vertx the vertx instance
   * @param options the ssh options
   * @return the term server
   */
  static TermServer createSSHTermServer(Vertx vertx, SSHTermOptions options) {
    return new SSHTermServer(vertx, options);
  }

  /**
   * Create a term server for the Telnet protocol.
   *
   * @param vertx the vertx instance
   * @param options the telnet options
   * @return the term server
   */
  static TermServer createTelnetTermServer(Vertx vertx, TelnetTermOptions options) {
    return new TelnetTermServer(vertx, options);
  }

  /**
   * Set the term handler that will receive incoming client connections. When a remote terminal connects
   * the {@code handler} will be called with the {@link Term} which can be used to interact with the remote
   * terminal.
   *
   * @param handler the term handler
   * @return this object
   */
  @Fluent
  TermServer termHandler(Handler<Term> handler);

  /**
   * Bind the term server, the {@link #termHandler(Handler)} must be set before.
   *
   * @return this object
   */
  @Fluent
  default TermServer listen() {
    return listen(null);
  }

  /**
   * Bind the term server, the {@link #termHandler(Handler)} must be set before.
   *
   * @param listenHandler the listen handler
   * @return this object
   */
  @Fluent
  TermServer listen(Handler<AsyncResult<TermServer>> listenHandler);

  /**
   * The actual port the server is listening on. This is useful if you bound the server specifying 0 as port number
   * signifying an ephemeral port
   *
   * @return the actual port the server is listening on.
   */
  int actualPort();

  /**
   * Close the server. This will close any currently open connections. The close may not complete until after this
   * method has returned.
   */
  void close();

  /**
   * Like {@link #close} but supplying a handler that will be notified when close is complete.
   *
   * @param handler the handler
   */
  void close(Handler<AsyncResult<Void>> handler);

}

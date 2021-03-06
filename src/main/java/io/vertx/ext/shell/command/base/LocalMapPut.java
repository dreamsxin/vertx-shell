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

package io.vertx.ext.shell.command.base;

import io.vertx.core.Vertx;
import io.vertx.core.cli.annotations.Argument;
import io.vertx.core.cli.annotations.Description;
import io.vertx.core.cli.annotations.Name;
import io.vertx.core.cli.annotations.Summary;
import io.vertx.core.shareddata.LocalMap;
import io.vertx.core.shareddata.SharedData;
import io.vertx.ext.shell.command.AnnotatedCommand;
import io.vertx.ext.shell.command.CommandProcess;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@Name("local-map-put")
@Summary("Put key/value in a local map")
public class LocalMapPut extends AnnotatedCommand {

  private String map;
  private String key;
  private String value;

  @Argument(index = 0, argName = "map", required = false)
  @Description("the local shared map name")
  public void setMap(String map) {
    this.map = map;
  }

  @Argument(index = 1, argName = "key", required = false)
  @Description("the key to put")
  public void setKey(String key) {
    this.key = key;
  }

  @Argument(index = 2, argName = "value", required = false)
  @Description("the value to put")
  public void setValue(String value) {
    this.value = value;
  }

  @Override
  public void process(CommandProcess process) {
    Vertx vertx = process.vertx();
    SharedData sharedData = vertx.sharedData();
    LocalMap<Object, Object> localMap = sharedData.getLocalMap(map);
    localMap.put(key, value);
    process.end();
  }
}

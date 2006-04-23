/*
 * Copyright 2002-2005 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 

package org.springframework.ide.eclipse.beans.ui.editor.outline;

import java.util.ArrayList;

import org.eclipse.wst.sse.ui.internal.contentoutline.IJFaceNodeAdapter;
import org.eclipse.wst.xml.ui.internal.contentoutline.JFaceNodeAdapter;
import org.eclipse.wst.xml.ui.internal.contentoutline.JFaceNodeAdapterFactory;
import org.springframework.ide.eclipse.beans.ui.editor.BeansEditorUtils;
import org.w3c.dom.Node;

/**
 * Adapts a Spring beans node to a JFace viewer.
 */
public class BeansJFaceNodeAdapter extends JFaceNodeAdapter {

	public static final Class ADAPTER_KEY = IJFaceNodeAdapter.class;

	public BeansJFaceNodeAdapter(JFaceNodeAdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * Allowing the INodeAdapter to compare itself against the type allows it
	 * to return true in more than one case.
	 */
	public boolean isAdapterForType(Object type) {
		return type.equals(ADAPTER_KEY);
	}

	public boolean hasChildren(Object object) {
		if (BeansEditorUtils.isSpringStyleOutline()) {
			Node node = (Node) object;
			for (Node child = node.getFirstChild(); child != null;
											  child = child.getNextSibling()) {
				if (child.getNodeType() != Node.TEXT_NODE)
					return true;
			}
		} else {
			return super.hasChildren(object);
		}
		return false;
	}
	
	public Object[] getChildren(Object object) {
		if (BeansEditorUtils.isSpringStyleOutline()) {
			Node node = (Node) object;
			if (node.getNodeType() == Node.DOCUMENT_NODE) {
				for (Node child = node.getFirstChild(); child != null;
											  child = child.getNextSibling()) {
					if (child.getNodeType() == Node.ELEMENT_NODE &&
										 "beans".equals(child.getNodeName())) {
						ArrayList children = new ArrayList();
						for (Node n = child.getFirstChild(); n != null;
													  n = n.getNextSibling()) {
							if (n.getNodeType() == Node.ELEMENT_NODE) {
								String nodeName = n.getNodeName();
								if ("alias".equals(nodeName) ||
											  "import".equals(nodeName) ||
											  "description".equals(nodeName) ||
											  "bean".equals(nodeName)) {
									children.add(n);
								}
							}
							else if (n.getNodeType() == Node.COMMENT_NODE) {
								children.add(n);
							}
						}
						return children.toArray();
					}
				}
			}
			ArrayList children = new ArrayList();
			for (Node child = node.getFirstChild(); child != null;
											  child = child.getNextSibling()) {
				Node n = child;
				if (n.getNodeType() != Node.TEXT_NODE) {
					children.add(n);
				}
			}
			return children.toArray();
		} else {
			return super.getChildren(object);
		}
	}

	public Object getParent(Object object) {
		if (BeansEditorUtils.isSpringStyleOutline()) {
			Node node = (Node) object;
			return node.getParentNode();
		} else {
			return super.getParent(object);
		}
	}
}

package com.lembed.unit.test.elaunch.commands;

import java.util.List;

import org.eclipse.cdt.core.dom.ast.ASTVisitor;
import org.eclipse.cdt.core.dom.ast.IASTAttribute;
import org.eclipse.cdt.core.dom.ast.IASTDeclarator;
import org.eclipse.cdt.core.dom.ast.IASTFunctionDeclarator;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.dom.ast.IBinding;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPFunction;
import org.eclipse.cdt.core.model.CModelException;
import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.cdt.core.model.ICElement;
import org.eclipse.cdt.core.model.ICProject;
import org.eclipse.cdt.core.model.ITranslationUnit;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import com.lembed.unit.test.core.utils.CProjectHelper;
import com.lembed.unit.test.elaunch.UnitBuilder;

public class SyncCommand extends AbstractCommand {

	private static class RunMakeJob extends SelectionJob {

		private final UnitBuilder unitBuilder;

		public RunMakeJob(String name) {
			super(name);
			unitBuilder = new UnitBuilder();
		}

		@Override
		protected void runResource(IResource resource, IProgressMonitor monitor) {
			// unitBuilder.processResource(resource, monitor);
			IProject project = resource.getProject();
			ICProject cproj = CoreModel.getDefault().create(project);
			try {
				List<ITranslationUnit> tus = CProjectHelper.getTranslationUnitsOfProject(cproj);
				if (tus == null) {
					return;
				}
				
				for (ITranslationUnit u : tus) {
					log(u.getLocation().toString());

					IASTTranslationUnit ast = u.getAST();
					ast.accept(new ASTVisitor() {
						{
							shouldVisitDeclarators = true;
						}

						@Override
						public int visit(IASTDeclarator declarator) {

							if (declarator instanceof IASTFunctionDeclarator) {
								log(declarator.getName().toString());
								IASTFunctionDeclarator fd = (IASTFunctionDeclarator)declarator;

								IBinding binding = declarator.getName().resolveBinding();
								if(binding instanceof ICPPFunction) {
									ICPPFunction cpp = (ICPPFunction) binding;
									log(cpp.getName() +declarator.getRawSignature() );
									
								}
							}
							return super.visit(declarator);
						}

					});

				}

			} catch (CoreException e) {
				e.printStackTrace();
			}

			try {
				List<ICElement> blist = CProjectHelper.findElements(cproj);
				for (ICElement b : blist) {
					//log(b.getPath().toFile().getAbsolutePath() + b.getElementType());
				}
			} catch (CModelException e) {
				e.printStackTrace();
			}

			try {
				project.accept(new IResourceVisitor() {

					@Override
					public boolean visit(IResource resource) throws CoreException {
						//log(resource.getFullPath().toString());
						return true;
					}

				});
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	@Override
	SelectionJob getJob() {
		return new RunMakeJob("make");
	}

	private static void log(String msg) {
		System.out.println(msg);
	}
}

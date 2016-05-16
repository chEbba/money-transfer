package org.chebba.mt.http.swagger

import org.chebba.mt.http._
import org.chebba.mt.http.matching._
import org.chebba.mt.http.path.Path
import play.api.libs.json.Json

import scala.concurrent.Future

/**
  * @author Kirill chEbba Chebunin
  */
object RestHandler {
  def apply(controllers: Seq[RestController], endpoint: String = "/") = new NestedPartialRequestHandler {
    private val controllerHandlers = controllers.map(new ResourceHandler(_))

    private val docs = new PartialRequestHandler {
      private val listing = ResourceListing(controllerHandlers.map(r => Resource(r.basePath.toString, None)), apiVersion = Some("1.0"))
      private val listingResponse = Future.successful(HttpResp(
        body = Some(Content(Json.toJson(listing).toString)),
        headers = Map(ContentType(_.JSON))
      ))
      def tryHandle: PartialFunction[HttpReq, Future[HttpResp]] = {
        case GET -> "api-docs" => listingResponse
      }
    }

    def handlers: Seq[PartialRequestHandler] = docs +: controllerHandlers
  }

  class ResourceHandler(controller: RestController, endpoint: String = "/") extends PartialRequestHandler {
    val (basePath, declaration) = {
      val operations = controller.operations.map(_._1)
      val apis = operations.groupBy(_.method.path).toSeq.map { case (path, defs) =>
        Api(path.toString, defs.map(_.operation).sortBy(_.method), None)
      }.sortBy(_.path)

      val models = operations.flatMap(_.models).distinct.sortBy(_.id)

      val basePath = operations.map(_.method.path).foldLeft[Option[Path]](None)((base, path) => base match {
        case Some(x) => Some(x.base(path))
        case None => Some(path)
      }).map(_.staticPrefix).getOrElse(Path())

      basePath -> ApiDeclaration(
        endpoint,
        basePath.toString,
        apis,
        models.toList.map(m => (m.id, m)).toMap
      )
    }

    private val DOC_PATH = "api-docs" + basePath.toString
    private val declarationResponse = Future.successful(HttpResp(
      body = Some(Content(Json.toJson(declaration).toString)),
      headers = Map(ContentType(_.JSON))
    ))

    def tryHandle: PartialFunction[HttpReq, Future[HttpResp]] = {
      val docs:PartialFunction[HttpReq, Future[HttpResp]] = {
        case GET -> DOC_PATH => declarationResponse
      }
      val methods = Function.unlift { req: HttpReq =>
        controller.operations.flatMap { case (op, handler) =>
          if (op.method.httpMethod == req.method) {
            op.method.path.parse(req.path).map(MethodRequest(op.method, req, _) -> handler)
          } else {
            None
          }
        }.headOption.map { case (m, handler) =>
          handler(m)
        }
      }

      docs orElse methods
    }
  }
}
